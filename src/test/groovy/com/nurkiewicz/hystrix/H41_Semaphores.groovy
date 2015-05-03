package com.nurkiewicz.hystrix

import spock.lang.Specification


/**
 * Used when request volume is really big
 * Thread pool overhead is intolerable
 * Use only when underlying action is known to be fast
 * ...or encapsulate other Hystrix command
 */
class H41_Semaphores extends Specification {

}