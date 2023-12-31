package com.cyecize.app.web;

import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.error.ErrorResponse;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.solet.SoletConstants;
import com.cyecize.solet.SoletOutputStream;
import com.cyecize.summer.areas.routing.exceptions.HttpNotFoundException;
import com.cyecize.summer.areas.security.exceptions.UnauthorizedException;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.exceptions.ObjectBindingException;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.routing.ExceptionListener;
import com.cyecize.summer.utils.PathUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GlobalErrorController {

    @Configuration(SoletConstants.SOLET_CONFIG_ASSETS_DIR)
    private final String assetsDir;

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    @Configuration(SoletConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY)
    private final JavacheConfigService serverCfg;

    private Path indexPath1;
    private Path indexPath2;

    @PostConstruct
    public void init() {
        this.indexPath1 = Path.of(PathUtils.appendPath(this.assetsDir, "/index.html"));

        final String path2 = PathUtils.appendPath(
                this.workingDir,
                this.serverCfg.getConfigParamString(JavacheConfigValue.APP_RESOURCES_DIR_NAME)
        );
        this.indexPath2 = Path.of(path2, "/index.html");
    }

    /**
     * Logic for forwarding requests to the frontend. If index.html exists in the assets' dir, then
     * the FE is deployed. In that case, then render the HTML content of that file regardless of the
     * URL, FE will handle it. Otherwise, print an error.
     */
    @ExceptionListener(HttpNotFoundException.class)
    public Object handleNotFoundException(HttpSoletRequest req,
            HttpSoletResponse res,
            HttpNotFoundException ex) throws IOException {
        final Path indexPath;
        if (Files.exists(this.indexPath1)) {
            indexPath = this.indexPath1;
        } else if (Files.exists(this.indexPath2)) {
            indexPath = this.indexPath2;
        } else {
            indexPath = null;
        }

        if (indexPath != null) {
            res.setStatusCode(HttpStatus.OK);
            // Old way of serving files. Not recommended since it loads the whole file in memory
            // ----> final String fileContents = new String(Files.readAllBytes(indexPath));
            // ----> return new ModelAndView("index.html.twig", Map.of("data", fileContents));
            this.downloadHtmlFile(indexPath, res);
            return null;
        }

        res.setStatusCode(HttpStatus.NOT_FOUND);
        ex.printStackTrace();
        return "Page not found!";
    }

    @ExceptionListener(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException exception,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return this.createErrorResponse(request, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionListener(ConstraintValidationException.class)
    public List<FieldError> constraintErrs(ConstraintValidationException ex,
            BindingResult bindingResult,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        log.trace("Constraint errors: {}", bindingResult.getErrors());
        return bindingResult.getErrors();
    }

    @ExceptionListener(value = ApiException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleApiException(ApiException exception,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return this.createErrorResponse(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionListener(value = ObjectBindingException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleObjectBingingException(ObjectBindingException ex,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return this.createErrorResponse(
                request,
                HttpStatus.BAD_REQUEST,
                ex.getCause().getMessage()
        );
    }

    @ExceptionListener(value = NotFoundApiException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleNotFoundApiException(NotFoundApiException exception,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return this.createErrorResponse(request, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ErrorResponse createErrorResponse(HttpSoletRequest request, HttpStatus status,
            String message) {
        return new ErrorResponse(request.getRequestURI(), status, message);
    }

    private void downloadHtmlFile(Path filePath, HttpSoletResponse response) {
        final File file = filePath.toFile();

        try {
            response.addHeader("Content-Type", "text/html");
            response.addHeader("Content-Disposition", "inline;");
            response.addHeader("Content-Length", file.length() + "");

            final SoletOutputStream outputStream = response.getOutputStream();
            final byte[] buff = new byte[2048];
            try (final FileInputStream fileInputStream = new FileInputStream(file)) {
                while (fileInputStream.available() > 0) {
                    final int read = fileInputStream.read(buff);
                    outputStream.write(buff, 0, read);
                }
            }
        } catch (IOException ex) {
            throw new ApiException("Could not download file!");
        }
    }
}
