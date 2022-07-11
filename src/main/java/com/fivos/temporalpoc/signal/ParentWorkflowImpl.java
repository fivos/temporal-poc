package com.fivos.temporalpoc.signal;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

public class ParentWorkflowImpl implements ParentWorkflow {
	private final ActivityOptions options = ActivityOptions.newBuilder().validateAndBuildWithDefaults();
	private final DatabaseActivity databaseActivity =
		Workflow.newActivityStub(DatabaseActivity.class, options);

	@Override
	public void runWorkflow() {
		databaseActivity.loadData();
	}

	@Override
	public void sendSignal(Integer value) {
		ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
			.setTaskQueue(Shared.SIGNAL_TASK_QUEUE)
			.setWorkflowId(String.valueOf(value))
			.setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
			.build();

		ChildWorkflow child = Workflow.newChildWorkflowStub(ChildWorkflow.class, options);
		child.runWorkflow();
	}
}
