/*
 * This file is part of the PSL software.
 * Copyright 2011 University of Maryland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.umd.cs.psl.sampler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ujmp.core.Matrix;

import edu.umd.cs.psl.evaluation.process.RunningProcess;
import edu.umd.cs.psl.model.kernel.GroundKernel;

public class UniformSampler extends AbstractHitAndRunSampler {

	private static final Logger log = LoggerFactory.getLogger(UniformSampler.class);
	
	private double total;
	
	public UniformSampler(RunningProcess p) {
		this(p, defaultMaxNoSteps, defaultSignificantDigits);
	}
	
	public UniformSampler(RunningProcess p,int maxNoSteps) {
		this(p,maxNoSteps, defaultSignificantDigits);
	}
 	
	public UniformSampler(RunningProcess p, int maxNoSteps, int significantDigits) {
		super(p, maxNoSteps, significantDigits);
		total = 0.0;
	}
	
	public double getLogAverageDensity() {
		return Math.log(total) - Math.log(getNoSamples());
	}
	
	@Override
	protected double sampleAlpha(Matrix direction, Matrix Aobj, Matrix objConst, double alphaLow, double alphaHigh) {
		double alpha = alphaLow + (alphaHigh - alphaLow) * Math.random();
		log.trace("Sampled alpha {}", alpha);
		return alpha;
	}
	
	@Override
	protected void processCurrentPoint(Iterable<GroundKernel> groundKernels) {
		double incompatibility = 0.0;
    	for (GroundKernel gk : groundKernels) {
    		incompatibility += gk.getIncompatibility();
    	}
    	total += Math.exp(-1 * incompatibility);
	}
	
}