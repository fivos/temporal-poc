package com.fivos.temporalpoc.batch;

import java.util.Random;
import java.util.stream.Stream;

import io.temporal.activity.Activity;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class BatchDatabaseActivityImpl implements BatchDatabaseActivity {
	@Override
	public Boolean loadData() {
		WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
		WorkflowClient client = WorkflowClient.newInstance(service);
		BatchParentWorkflow workflow = client.newWorkflowStub(
			BatchParentWorkflow.class, Activity.getExecutionContext().getInfo().getWorkflowId());

		try (Stream<Integer> stream = query()) {
			stream.forEach(workflow::receiveValue);
		}

		workflow.dataLoadingCompleted();
		return true;
	}

	private Stream<Integer> query() {
		// TODO: Replace with DB query
		Random random = new Random();
		return random.ints(30).boxed();
	}
}
