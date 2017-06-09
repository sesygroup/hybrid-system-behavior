/*
 * Copyright 2017 Software Engineering and Synthesis Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sesygroup.choreography.hybridsystembehavior;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class ValidationMessages {
   public static final String IS_NULL_EXCEPTION_MESSAGE = "The element '%s' must not be null";
   public static final String IS_ELEMENTS_NULL_EXCEPTION_MESSAGE = "The collection '%s' contains null at position %d";
   public static final String IS_EMPTY_MAP_EXCEPTION_MESSAGE = "The map '%s' must not be empty";
   public static final String IS_NOT_EQUAL_COLLETION_EXCEPTION_MESSAGE
         = "The collections '%s' and '%s' contain different elements, respectivelly %s and %s";
   public static final String IS_NOT_GRATER_OR_EQUAL_EXCEPTION_MESSAGE
         = "The element '%s' must be greater than or equal %d but was: %d";
   public static final String IS_NOT_EMPTY_INTERSECTION_INPUT_MESSAGES_EXCEPTION_MESSAGE
         = "The intersection of input messages between participants %s:%s and %s:%s must be empty";
   public static final String IS_NOT_EMPTY_INTERSECTION_OUTPUT_MESSAGES_EXCEPTION_MESSAGE
         = "The intersection of output messages between participants %s:%s and %s:%s must be empty";
   public static final String IS_NOT_IN_BOUNDARIES_EXCEPTION_MESSAGE
         = "The value of '%s':%d is not in inclusive boundaries [start:%d, end:%d], it contains %s";
   public static final String IS_NOT_EQUAL_CRDINALITY_COLLETION_EXCEPTION_MESSAGE
         = "The collections '%s' and '%s' have different cardinality, respectivelly %s:%d and %s:%d";

   public static final String IS_STATE_NOT_IN_SET_OF_STATES_EXCEPTION_MESSAGE
         = "The state %s is not contained in the set of states";

   // -----------------------------------------------------------------------

   /**
    * <p>
    * {@code ValidationMessages} instances should NOT be constructed in standard programming. Instead, the class should
    * be used statically.
    * </p>
    *
    * <p>
    * This constructor is public to permit tools that require a JavaBean instance to operate.
    * </p>
    */
   public ValidationMessages() {
      super();
   }

}
