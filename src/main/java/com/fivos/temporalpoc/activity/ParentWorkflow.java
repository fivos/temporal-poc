package com.fivos.temporalpoc.activity;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ParentWorkflow {

	@WorkflowMethod
	void runWorkflow();
}
