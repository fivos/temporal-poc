package com.fivos.temporalpoc.batch;

import java.util.Set;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface BatchChildWorkflow {

	@WorkflowMethod
	Boolean executeWorkflow(Set<Integer> values);
}
