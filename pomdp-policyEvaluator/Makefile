.PHONY: build run clean
build:
	javac -d build/ -cp ".:lib/burlap_v1.jar:lib/commons-math3-3.5.jar:lib/Jama-1.0.3.jar:lib/jcommon-1.0.17.jar:lib/jfreechart-1.0.14.jar" -sourcepath src/ src/pomdp/policyevaluator/PomdpPolicyEvaluator.java
run:
	java -cp "build:lib/burlap_v1.jar:lib/commons-math3-3.5.jar:lib/Jama-1.0.3.jar:lib/jcommon-1.0.17.jar:lib/jfreechart-1.0.14.jar" pomdp.policyevaluator.PomdpPolicyEvaluator ${npolicy} ${epolicy}
clean:
	rm -rf ./build/*