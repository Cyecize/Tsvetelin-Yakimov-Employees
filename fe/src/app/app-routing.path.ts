import {RouteConfig} from './shared/routing/route-config';

export class AppRoutingPath {
  public static readonly NOT_FOUND: RouteConfig = new RouteConfig('not-found', null);
  public static readonly HOME: RouteConfig = new RouteConfig('', null);
  public static readonly MOST_COMMON_EMPLOYEES: RouteConfig = new RouteConfig('most-common-employees', null);

}
