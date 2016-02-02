package com.newmarket.force.ant.dsl

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


public class JUnitReport : EmptyTag() {
    public fun testSuite(
        name: String = "",
        tests: Int = 0,
        errors: Int = 0,
        failures: Int = 0,
        time: Double = 0.0,
        timestamp: LocalDateTime = LocalDateTime.now(),
        init: TestSuite.() -> Unit = {}): TestSuite {

        val suite = initTag(TestSuite(), init)
        suite.name = name
        suite.tests = tests
        suite.errors = errors
        suite.failures = failures
        suite.time = time
        suite.timestamp = timestamp
        return suite
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        render(builder, "")
        return builder.toString()
    }
}

public class TestSuite : Tag("testsuite") {
    public final val testCases: Iterable<TestCase>
        get() = children.filterIsInstance<TestCase>()

    public var errors: Int
        get() = attributes["errors"]!!.toInt()
        set(value) { attributes["errors"] = value.toString() }

    public var failures: Int
        get() = attributes["failures"]!!.toInt()
        set(value) { attributes["failures"] = value.toString() }

    public var tests: Int
        get() = attributes["tests"]!!.toInt()
        set(value) { attributes["tests"] = value.toString() }

    public var name: String
        get() = attributes["name"]!!
        set(value) { attributes["name"] = value }

    public var time: Double
        get() = attributes["time"]!!.toDouble()
        set(value) { attributes["time"] = value.toString() }

    public var timestamp: LocalDateTime
        get() = LocalDateTime.parse(attributes["timestamp"]!!, DateTimeFormatter.ISO_DATE_TIME)
        set(value) { attributes["timestamp"] = DateTimeFormatter.ISO_DATE_TIME.format(value) }

    public fun properties(init: Properties.() -> Unit = {}): Properties =
        initTag(Properties(), init)

    public fun testCase(
        className: String = "",
        name: String = "",
        time: Double = 0.0,
        init: TestCase.() -> Unit = {}): TestCase {

        val case = initTag(TestCase(), init)
        case.className = className
        case.name = name
        case.time = time
        return case
    }
}

public class Properties: Tag("properties") {
    public fun fromMap(properties: Map<String, String>) =
        properties.forEach { property(name = it.key, value = it.value) }

    public fun property(
        name: String = "",
        value: String = "",
        init: Property.() -> Unit = {}): Property {

        val property = initTag(Property(), init)
        property.name = name
        property.value = value
        return property
    }
}

public class Property: Tag("property") {
    public var name: String
        get() = attributes["name"]!!
        set(value) { attributes["name"] = value }

    public var value: String
        get() = attributes["value"]!!
        set(value) { attributes["value"] = value }
}

public class TestCase : Tag("testcase") {
    public var className: String
        get() = attributes["classname"]!!
        set(value) { attributes["classname"] = value }

    public var name: String
        get() = attributes["name"]!!
        set(value) { attributes["name"] = value }

    public var time: Double
        get() = attributes["time"]!!.toDouble()
        set(value) { attributes["time"] = value.toString() }

    public fun failure(
        message: String = "",
        type: String = "",
        init: Failure.() -> Unit = {}): Failure {

        val failure = initTag(Failure(), init)
        failure.message = message
        failure.type = type
        return failure
    }
}

public class Failure: TagWithCharacterData("failure") {
    public var message: String
        get() = attributes["message"]!!
        set(value) { attributes["message"] = value }

    public var type: String
        get() = attributes["type"]!!
        set(value) { attributes["type"] = value }
}