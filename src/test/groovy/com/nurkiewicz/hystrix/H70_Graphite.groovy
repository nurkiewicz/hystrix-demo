package com.nurkiewicz.hystrix

import spock.lang.Specification


/**
 * Long-term history of Hystrix statistics
 *
 * $ docker run -p 8081:80 -p 2003:2003 --name grafana kamon/grafana_graphite
 * $ docker start -a grafana
 */
class H70_Graphite extends Specification {

}