package com.fivos.temporalpoc.signal;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ParentWorkflow {

	@WorkflowMethod
	void runWorkflow();

	@WorkflowMethod
	void sendSignal(Integer value);
}
