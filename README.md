# auto-names
## Version 2. Draft (Experimental)
- Refactor and improve the project code.
- Parameterize values such as the model, output, etc

## Version 1. Stable version of the project.
**Abstract**. In Object-oriented Programming (OOP), the Cognitive Complexity (CC) of software is a metric of the difficulty associated with un-derstanding and maintaining the source code. This is usually measuredat the method level, taking into account the number of control flow sen-tences and their nesting level. One way to reduce the CC associated toa method is by extracting code into new methods without altering anyexisting functionality. However, this involves deciding on new names thatare representative of the functionality of the extracted code. This workfocuses on large language models to automate the process of assigningnew methods names after refactoring operations in software projects.We use the OpenAI Chat API with the textdavinci003 model in order toperform coding tasks. This work studies the capability of this techniquefor assigning names to new extracted methods during the evolution of acode base. Such evolution comprises continuous extraction operations tostudy how the method name semantics stability evolves. We found theprecision of the model to be highly acceptable, achieving in many casesa level similar to that of a human. However, there are also a few casesin which it fails to provide appropriate names or does not even providea name inside the indicated standards. 

## Work and experiment
This repository has been created to support the work and an experiment.
It makes use of the OpenAI *text-davinci-003* model to make method names predictions.

## Setup
In order to run the experiment you will need an OpenAI API key to authorize the REST HTTP requests to the AI model. The key has to be stored in the `OPEN_AI_KEY` environment variable.

Install poetry in your OS
- Run `poetry shell` to activate the environment
- Run `poetry install` to install all the dependencies
- Run `python -m spacy download en_core_web_md` so the dependency SpaCy has access to the english model

## Run the script
To run the script, just use the command:

```python run_experiment.py <input folder> <model> <output folder>```

To replicate the experiment from the paper (Version 1 stable), use the command:

```python run_experiment.py dataset/Archive0_no_nested text-davinci-003 output```

The structure of the input folder must be
```
- method1.Original.java
- method1.Extraction1.java
- method1.Extraction2.java
...
- method1.ExtractionN.java
...
...
- method2.Original.java
- method2.Extraction1.java
- method2.Extraction2.java
...
- method2.ExtractionN.java
```

The reasoning: 
The project aims to automatically predict the name for methods after having applied some extraction-refactoring operations of code. For every method we can perform consecutive extractions of code. Each extraction should provide more information and context to fine tune the method name more precisely.
method.Original.java includes the original code method, method.Extractoin1.java includes the method after having applied one extraction, etc
  
## Output results
The results of the predictions will be stored in a folder `/Output`
Each method (Original and its extractions) will produce a JSON result file inside folder.


Example:

- MOEAFramework.src.org.moeaframework.Instrumenter.instrument.Original.java
- MOEAFramework.src.org.moeaframework.Instrumenter.instrument.Extraction1.java
- MOEAFramework.src.org.moeaframework.Instrumenter.instrument.Extraction2.java


will produce the file `output/MOEAFramework.src.org.moeaframework.Instrumenter.instrument.json` with the results.
The shape of the results contains the following information:

- Metadata about the original method and the extractions.
- Predictions for the method after each consecutive refactor extraction operation.
- Computed similarities between each prediction and the original method.
- Original code, code after each extraction and predicted name for all the extractions.

Next, it is displayed the JSON output result for the method `MOEAFramework.src.org.moeaframework.Instrumenter.instrument` which has gone through 2 extractions operations.
```{json}
{
    "original": {
        "method_name": "samplePopulation",
        "code": "/**  * Samples a new population. */private void samplePopulation(){  boolean feasible=true;  int N=problem.getNumberOfVariables();  if ((iteration - lastEigenupdate) > 1.0 / ccov / N/ 5.0) {    eigendecomposition();  }  if (checkConsistency) {    testAndCorrectNumerics();  }  population.clear();  for (int i=0; i < lambda; i++) {    Solution solution=problem.newSolution();    if (diagonalIterations >= iteration) {      do {        feasible=true;        for (int j=0; j < N; j++) {          RealVariable variable=(RealVariable)solution.getVariable(j);          double value=xmean[j] + sigma * diagD[j] * PRNG.nextGaussian();          if (value < variable.getLowerBound() || value > variable.getUpperBound()) {            feasible=false;            break;          }          variable.setValue(value);        }      } while (!feasible);    } else {      double[] artmp=new double[N];      do {        feasible=true;        for (int j=0; j < N; j++) {          artmp[j]=diagD[j] * PRNG.nextGaussian();        }        for (int j=0; j < N; j++) {          RealVariable variable=(RealVariable)solution.getVariable(j);          double sum=0.0;          for (int k=0; k < N; k++) {            sum+=B[j][k] * artmp[k];          }          double value=xmean[j] + sigma * sum;          if (value < variable.getLowerBound() || value > variable.getUpperBound()) {            feasible=false;            break;          }          variable.setValue(value);        }      } while (!feasible);    }    population.add(solution);  }  iteration++;}",
        "extraction_name": null,
        "predictions": [
            "samplePopulation_checkFeasibility",
            "samplePopulation"
        ],
        "similarities": [
            0.8243320889090123,
            1.0
        ],
        "file_name": "MOEAFramework.src.org.moeaframework.algorithm.CMAES.samplePopulation.Original.java"
    },
    "extractions": [
        {
            "method_name": "samplePopulation",
            "code": "/**  * Samples a new population. */private void samplePopulation(){  boolean feasible=true;  int N=problem.getNumberOfVariables();  if ((iteration - lastEigenupdate) > 1.0 / ccov / N/ 5.0) {    eigendecomposition();  }  if (checkConsistency) {    testAndCorrectNumerics();  }  population.clear();  for (int i=0; i < lambda; i++) {    Solution solution=problem.newSolution();    if (diagonalIterations >= iteration) {      do {        feasible=true;        for (int j=0; j < N; j++) {          RealVariable variable=(RealVariable)solution.getVariable(j);          double value=xmean[j] + sigma * diagD[j] * PRNG.nextGaussian();          if (value < variable.getLowerBound() || value > variable.getUpperBound()) {            feasible=false;            break;          }          variable.setValue(value);        }      } while (!feasible);    } else {      double[] artmp=new double[N];      do {        feasible=samplePopulation_extraction_2(N,solution,artmp);      } while (!feasible);    }    population.add(solution);  }  iteration++;}",
            "extraction_name": "Extraction1",
            "prediction": "samplePopulation_checkFeasibility",
            "similarity": null,
            "file_name": "MOEAFramework.src.org.moeaframework.algorithm.CMAES.samplePopulation.Extraction1.java",
            "updated_code": "/**  * Samples a new population. */private void samplePopulation(){  boolean feasible=true;  int N=problem.getNumberOfVariables();  if ((iteration - lastEigenupdate) > 1.0 / ccov / N/ 5.0) {    eigendecomposition();  }  if (checkConsistency) {    testAndCorrectNumerics();  }  population.clear();  for (int i=0; i < lambda; i++) {    Solution solution=problem.newSolution();    if (diagonalIterations >= iteration) {      do {        feasible=true;        for (int j=0; j < N; j++) {          RealVariable variable=(RealVariable)solution.getVariable(j);          double value=xmean[j] + sigma * diagD[j] * PRNG.nextGaussian();          if (value < variable.getLowerBound() || value > variable.getUpperBound()) {            feasible=false;            break;          }          variable.setValue(value);        }      } while (!feasible);    } else {      double[] artmp=new double[N];      do {        feasible=samplePopulation_checkFeasibility(N,solution,artmp);      } while (!feasible);    }    population.add(solution);  }  iteration++;}"
        },
        {
            "method_name": "samplePopulation",
            "code": "/**  * Samples a new population. */private void samplePopulation(){  boolean feasible=true;  int N=problem.getNumberOfVariables();  if ((iteration - lastEigenupdate) > 1.0 / ccov / N/ 5.0) {    eigendecomposition();  }  if (checkConsistency) {    testAndCorrectNumerics();  }  population.clear();  for (int i=0; i < lambda; i++) {    Solution solution=problem.newSolution();    if (diagonalIterations >= iteration) {      do {        feasible=true;        feasible=samplePopulation_extraction_1(feasible,N,solution);      } while (!feasible);    } else {      double[] artmp=new double[N];      do {        feasible=samplePopulation_extraction_2(N,solution,artmp);      } while (!feasible);    }    population.add(solution);  }  iteration++;}",
            "extraction_name": "Extraction2",
            "prediction": "samplePopulation_checkVariables",
            "similarity": null,
            "file_name": "MOEAFramework.src.org.moeaframework.algorithm.CMAES.samplePopulation.Extraction2.java",
            "updated_code": "/**  * Samples a new population. */private void samplePopulation(){  boolean feasible=true;  int N=problem.getNumberOfVariables();  if ((iteration - lastEigenupdate) > 1.0 / ccov / N/ 5.0) {    eigendecomposition();  }  if (checkConsistency) {    testAndCorrectNumerics();  }  population.clear();  for (int i=0; i < lambda; i++) {    Solution solution=problem.newSolution();    if (diagonalIterations >= iteration) {      do {        feasible=true;        feasible=samplePopulation_checkVariables(feasible,N,solution);      } while (!feasible);    } else {      double[] artmp=new double[N];      do {        feasible=samplePopulation_checkFeasibility(N,solution,artmp);      } while (!feasible);    }    population.add(solution);  }  iteration++;}"
        }
    ],
    "file_name": null
}
```

## Re-run specific methods only
If a json file with the results for a method exists, the script will not produce a new result. You need to remove/rename the file to get a new one. Thus, only the removed/renames one will be regenerated. (This is useful if you change the prompt to send to the model REST API).

## text-davinci-003 prompt
In the file open_api_model.py there are multiple versions of the prompt that can be sent. If a new one is needed just add it to the file, the predictions to the model can be called specifying the explicit version or changing the default in the calls to the model.


Example:
```
    "v3": lambda x, y, z: """Given the initial java code: '{original_code}' and following code: '{extraction_code}' 
    which functionally is the same as the initial code, but having applied an extract refactor operation. 
    Can you give an meaningful name for the extracted method '{method_to_compute}'? 
    (Please consider following points: 1:Provide the method name without parentheses. 2:Do not use the keyword \'extraction\' means 
    that the content of the method to predict was extracted from the initial code, so \'extracted\' keyword should not be part of 
    method name. 3:Provide a valid method name.)"""
                .format(original_code = x, extraction_code = y, method_to_compute = z),
    "v4": lambda x, y, z: """Given the initial java code: '{original_code}' and its refactored version: '{extraction_code}' 
    Can you give a name for the extracted method '{method_to_compute}'?. Provide the method name without parentheses. Avoid the extract operation bias for the the method name prediction. Use java notation for the method name (for example, do not use _).
    
    
    "v5": lambda x, y, z: .... <------------------------------------------------------ YOUR NEW PROMPT
    
```

## Calculate metrics
The metrics described in the experiment can all be represented using the file calculate_metrics.py

This file will scan the `output` folder to produce the graphical results.
