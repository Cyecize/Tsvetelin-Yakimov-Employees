import {FieldError} from '../field-error/field-error';
import {HttpStatus} from '../http/http-status';
import {Observable} from 'rxjs';
import {isArray} from 'rxjs/internal-compatibility';

export class FieldErrorWrapper<T> {

  constructor(private delegate: () => Observable<T>) {
  }

  async execute(): Promise<WrappedResponse<T>> {
    try {
      return new WrappedResponse<T>(true, [], await this.delegate.call(null).toPromise());
    } catch (err: any) {
      if (err.status === HttpStatus.NOT_ACCEPTABLE && isArray(err.error)) {
        return new WrappedResponse<T>(false, err.error as FieldError[]);
      }

      return new WrappedResponse<T>(
        false,
        [{
          message: err.error?.message || 'something.went.wrong',
          fieldName: ''
        }]
      );
    }
  }
}

export class WrappedResponse<T> {
  constructor(public readonly isSuccess: boolean,
              public readonly errors: FieldError[],
              private readonly resp?: T) {
  }

  get response(): T {
    // @ts-ignore
    return this.resp;
  }
}
