package com.fivos.temporalpoc.signal;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

public class SignalParentWorkflowImpl implements SignalParentWorkflow {
	private final ActivityOptions options = ActivityOptions.newBuilder()
		.setStartToCloseTimeout(Duration.ofMinutes(10))
		.build();

	private final DatabaseActivity databaseActivity =
		Workflow.newActivityStub(DatabaseActivity.class, options);

	@Override
	public void executeWorkflow() {
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
		child.executeWorkflow();
	}
}
