package com.fivos.temporalpoc.signal;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SignalParentWorkflow {

	@WorkflowMethod
	void executeWorkflow();

	@SignalMethod
	void sendSignal(Integer value);

	@SignalMethod
	void dataLoadingCompleted();
}
