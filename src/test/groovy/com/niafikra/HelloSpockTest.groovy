package com.niafikra

import spock.lang.Specification
class HelloSpockTest extends Specification {
	def "can you figure out what I'm up to?"() {
		expect:
		name.size() == length

		where:
        name    | length
        "kirk"  | 4
        "spock" | 5
        "scotty"| 6

	}

    def "mocking things up"(){

    }
}
