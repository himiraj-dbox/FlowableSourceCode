/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flowable.engine.impl.persistence.entity;

import java.util.Date;

import org.flowable.common.engine.impl.persistence.entity.Entity;

/**
 * @author Joram Barrez
 */
public interface HistoricScopeInstanceEntity extends Entity {

    void markEnded(String deleteReason);

    String getProcessInstanceId();

    String getProcessDefinitionId();

    Date getStartTime();

    Date getEndTime();

    Long getDurationInMillis();

    void setProcessInstanceId(String processInstanceId);

    void setProcessDefinitionId(String processDefinitionId);

    void setStartTime(Date startTime);

    void setEndTime(Date endTime);

    void setDurationInMillis(Long durationInMillis);

    String getDeleteReason();

    void setDeleteReason(String deleteReason);

}
