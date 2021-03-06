package org.spockframework.smoke.extension

import org.spockframework.EmbeddedSpecification
import org.spockframework.runtime.InvalidSpecException

import spock.lang.See

@See("http://spockframework.org/spec")
class SeeExtension extends EmbeddedSpecification {
  def "adds attachment for spec-level @See annotation"() {
    def attachments = specificationContext.currentSpec.attachments

    expect:
    attachments.size() == 1
    attachments[0].name == "http://spockframework.org/spec"
    attachments[0].url == "http://spockframework.org/spec"
  }

  @See("http://spockframework.org/feature")
  def "adds attachment for feature-level @See annotation"() {
    def attachments = specificationContext.currentFeature.attachments

    expect:
    attachments.size() == 1
    attachments[0].name == "http://spockframework.org/feature"
    attachments[0].url == "http://spockframework.org/feature"
  }

  @See(["http://spockframework.org/one", "http://spockframework.org/two"])
  def "adds attachment for each URL"() {
    def attachments = specificationContext.currentFeature.attachments

    expect:
    attachments.size() == 2
    attachments[0].name == "http://spockframework.org/one"
    attachments[0].url == "http://spockframework.org/one"
    attachments[1].name == "http://spockframework.org/two"
    attachments[1].url == "http://spockframework.org/two"
  }

  def "barks if @See is used on anything other than a spec or feature"() {
    when:
    runner.runSpecBody(
"""
@See("http://foo")
def setup() {}

def feature() {
  expect: true
}
""")

    then:
    InvalidSpecException e = thrown()
    e.message.contains("may not be applied to fixture methods")
  }
}
