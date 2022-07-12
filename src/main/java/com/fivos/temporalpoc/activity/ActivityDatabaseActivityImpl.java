package com.fivos.temporalpoc.activity;

import java.util.Random;
import java.util.stream.Stream;

import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class ActivityDatabaseActivityImpl implements ActivityDatabaseActivity {
	@Override
	public Boolean loadData() {
		try (Stream<Integer> stream = query()) {
			stream.forEach(this::runActivityWorkflow);
		}
		return true;
	}

	private Stream<Integer> query() {
		// TODO: Replace with DB query
		Random random = new Random();
		return random.ints(1000).boxed();
	}

	private void runActivityWorkflow(Integer value) {
		System.out.println("Schedule activity workflow for " + value);
		WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
		WorkflowClient client = WorkflowClient.newInstance(service);

			WorkflowOptions options = WorkflowOptions.newBuilder()
				.setTaskQueue(ActivityShared.ACTIVITY_TASK_QUEUE)
				.setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE_FAILED_ONLY)
				.setWorkflowId("ActivityWorkflow-" + String.valueOf(value))
				.build();

			ActivityWorkflow activityWorkflow = client.newWorkflowStub(
				ActivityWorkflow.class,
				options);
		activityWorkflow.runWorkflow();
	}
}
