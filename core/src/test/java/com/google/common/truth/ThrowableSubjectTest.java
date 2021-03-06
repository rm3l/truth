/*
 * Copyright (c) 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.truth;

import static com.google.common.truth.Truth.assertThat;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Throwable} subjects.
 *
 * @author Kurt Alfred Kluever
 */
@RunWith(JUnit4.class)
public class ThrowableSubjectTest {
  @Test
  public void hasMessage() {
    NullPointerException npe = new NullPointerException("message");
    assertThat(npe).hasMessage("message");
  }

  @Test
  public void hasMessageThat() {
    NullPointerException npe = new NullPointerException("message");
    assertThat(npe).hasMessageThat().isEqualTo("message");
  }

  @Test
  public void hasMessageThat_null() {
    assertThat(new NullPointerException()).hasMessageThat().isNull();
    assertThat(new NullPointerException(null)).hasMessageThat().isNull();
  }

  @Test
  public void hasMessageThat_failure() {
    NullPointerException subject = new NullPointerException("message");
    try {
      assertThat(subject).hasMessageThat().isEqualTo("foobar");
      throw new Error("Expected to fail.");
    } catch (ComparisonFailure expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for java.lang.NullPointerException: "
                  + "expected:<[foobar]> but was:<[message]>");
      assertThat(expected.getCause()).isEqualTo(subject);
    }
  }

  @Test
  public void hasMessageThat_MessageHasNullMessage_failure() {
    try {
      assertThat(new NullPointerException("message")).hasMessageThat().isNull();
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for java.lang.NullPointerException: "
                  + "Not true that <\"message\"> is null");
    }
  }

  @Test
  public void hasMessageThat_Named_failure() {
    try {
      assertThat(new NullPointerException("message"))
          .named("NPE")
          .hasMessageThat()
          .isEqualTo("foobar");
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for NPE(java.lang.NullPointerException): "
                  + "expected:<[foobar]> but was:<[message]>");
    }
  }

  @Test
  public void hasMessageThat_NullMessageHasMessage_failure() {
    try {
      assertThat(new NullPointerException(null)).hasMessageThat().isEqualTo("message");
      throw new Error("Expected to fail.");
    } catch (AssertionError expected) {
      assertThat(expected.getMessage())
          .isEqualTo(
              "Unexpected message for java.lang.NullPointerException: "
                  + "Not true that <null> is equal to <\"message\">");
    }
  }

  @Test
  public void inheritedMethodChainsSubject() {
    NullPointerException expected = new NullPointerException("expected");
    NullPointerException actual = new NullPointerException("actual");
    try {
      assertThat(actual).isEqualTo(expected);
      throw new Error("Expected to fail.");
    } catch (AssertionError thrown) {
      assertThat(thrown.getCause()).isEqualTo(actual);
      /*
       * TODO(cpovirk): consider a special case for isEqualTo and isSameAs that adds |expected| as a
       * suppressed exception
       */
    }
  }
}
