package com.fivos.temporalpoc.batch;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BatchParentWorkflow {

	@WorkflowMethod
	void executeWorkflow();

	@SignalMethod
	void receiveValue(Integer value);

	@SignalMethod
	void dataLoadingCompleted();
}
