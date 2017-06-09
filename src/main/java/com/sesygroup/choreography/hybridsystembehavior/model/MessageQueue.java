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
package com.sesygroup.choreography.hybridsystembehavior.model;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

import com.sesygroup.choreography.concreteparticipantbehavior.model.Message;

/**
 *
 * @author Alexander Perucci (http://www.alexanderperucci.com/)
 *
 */
public class MessageQueue implements Serializable {
   private static final long serialVersionUID = -1175622121214486140L;
   private Integer capacity;
   private Queue<Message> queue;

   public MessageQueue() {
      super();
      capacity = null;
      queue = null;
   }

   public MessageQueue(final Integer capacity) {
      super();
      this.capacity = capacity;
      this.queue = capacity != 0
            ? new LinkedBlockingQueue<Message>(capacity)
            : null;
   }

   // TODO improve methods to change capacity of the queue, maintaining the contained messages.

   public final Integer getCapacity() {
      return capacity;
   }

   public final void setCapacity(final Integer capacity) {
      this.capacity = capacity;
   }

   public final Queue<Message> getQueue() {
      return queue;
   }

   public final void setQueue(final Queue<Message> queue) {
      this.queue = queue;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + capacity;
      result = prime * result + ((queue == null)
            ? 0
            : queue.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      MessageQueue other = (MessageQueue) obj;
      if (capacity != other.capacity) {
         return false;
      }
      if ((queue == null) ^ (other.queue == null)) {
         return false;
         /*
          * the default Message::equals checks also the type of the Message object, therefore we need to check the
          * Message name-equality bypassing the default equals method; otherwise we have that an InputMessage("m1") is
          * NOT equals to OutputMessage("m1"). Two lists are defined to be equal if they contain the same elements in
          * the same order
          */

      } else if (!(queue == null && other.queue == null)
            && !ListUtils.isEqualList(queue.stream().map(Message::getName).collect(Collectors.toList()),
                  other.queue.stream().map(Message::getName).collect(Collectors.toList()))) {
         return false;

      }
      return true;
   }

   @Override
   public String toString() {
      return queue == null
            ? "[]"
            : queue.toString().replaceAll("!", "").replaceAll("\\?", "");
   }
}
