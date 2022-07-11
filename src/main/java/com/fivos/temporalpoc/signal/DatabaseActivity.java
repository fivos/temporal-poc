package com.fivos.temporalpoc.signal;


import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface DatabaseActivity {

	@ActivityMethod
	Boolean loadData();

}
