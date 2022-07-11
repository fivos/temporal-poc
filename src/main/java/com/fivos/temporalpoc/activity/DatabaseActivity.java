package com.fivos.temporalpoc.activity;


import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DatabaseActivity {

	@ActivityMethod
	Boolean loadData();

}
