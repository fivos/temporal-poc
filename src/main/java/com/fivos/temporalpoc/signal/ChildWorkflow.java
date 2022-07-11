package com.fivos.temporalpoc.signal;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ChildWorkflow {

	@WorkflowMethod
	public void runWorkflow();
}
