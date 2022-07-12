package com.fivos.temporalpoc.signal;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SignalChildWorkflow {

	@WorkflowMethod
	public void executeWorkflow(Integer value);
}
