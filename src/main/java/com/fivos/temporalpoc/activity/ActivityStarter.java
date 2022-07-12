package com.fivos.temporalpoc.activity;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class ActivityStarter {

	public static final String ACTIVITY_TASK_QUEUE = "ACTIVITY_TASK_QUEUE";
	private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
	private static final WorkflowClient client = WorkflowClient.newInstance(service);
	private static final WorkerFactory factory = WorkerFactory.newInstance(client);

	public static void main(String[] args) {
		createWorker();

		WorkflowOptions parentWorkflowOptions =
			WorkflowOptions.newBuilder()
				.setWorkflowId("activityParentWorkflow")
				.setTaskQueue(ACTIVITY_TASK_QUEUE)
				.build();
		ActivityParentWorkflow parentWorkflowStub =
			client.newWorkflowStub(ActivityParentWorkflow.class, parentWorkflowOptions);

		parentWorkflowStub.executeWorkflow();

		sleep(4);
	}


	private static void createWorker() {
		Worker worker = factory.newWorker(ACTIVITY_TASK_QUEUE);
		worker.registerWorkflowImplementationTypes(ActivityParentWorkflowImpl.class, ActivityWorkflowImpl.class);
		worker.registerActivitiesImplementations(new ActivityDatabaseActivityImpl());

		factory.start();
	}

	private static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(0);
		}
	}

}
