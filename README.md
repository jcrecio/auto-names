# auto-names
## Version 2. Draft
- Refactor and improve the project code.
- Parameterize values such as the model, output, etc

## Version 1. 2023-07-06
**Abstract**. In Object-oriented Programming (OOP), the Cognitive Complexity (CC) of software is a metric of the difficulty associated with un-derstanding and maintaining the source code. This is usually measuredat the method level, taking into account the number of control flow sen-tences and their nesting level. One way to reduce the CC associated toa method is by extracting code into new methods without altering anyexisting functionality. However, this involves deciding on new names thatare representative of the functionality of the extracted code. This workfocuses on large language models to automate the process of assigningnew methods names after refactoring operations in software projects.We use the OpenAI Chat API with the textdavinci003 model in order toperform coding tasks. This work studies the capability of this techniquefor assigning names to new extracted methods during the evolution of acode base. Such evolution comprises continuous extraction operations tostudy how the method name semantics stability evolves. We found theprecision of the model to be highly acceptable, achieving in many casesa level similar to that of a human. However, there are also a few casesin which it fails to provide appropriate names or does not even providea name inside the indicated standards. 

(PDF) Naming Methods Automatically after Refactoring Operations. Available from: https://www.researchgate.net/publication/372288807_Naming_Methods_Automatically_after_Refactoring_Operations [accessed Jul 20 2023].


## Work and experiment
This code has been created to support the experiment described by the work *Naming Methods Automatically after Refactoring Operations*.
The paper for the work can be found [here](https://www.researchgate.net/publication/372288807_Naming_Methods_Automatically_after_Refactoring_Operations).

[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.8132928.svg)](https://doi.org/10.5281/zenodo.8132928)


It makes use of the *text-davinci-003* model to make method names predictions.

## Setup
In order to run the experiment you will need an OpenAI API key to authorize the REST HTTP requests to the model, then store your key in the `OPEN_AI_KEY` environment variable.

Install poetry in your OS
- Run `poetry shell` to activate the environment
- Run `poetry install` to install all the dependencies
- Run `python -m spacy download en_core_web_md` so the dependency SpaCy has access to the english model

## Run the script
To run the script, just use the command: ```python run_experiment.py <folder with java codes to predict>```

The structure of the folder must be
- method.Original.java
- method.Extraction1.java
- method.Extraction2.java
...
- method.ExtractionN.java


## Output results
The results of the predictions will be stored in a folder `/Output`
Each method (Original + its extractions) will produce a JSON result file inside folder.


Example:

- MOEAFramework.src.org.moeaframework.Instrumenter.instrument.Original.java
- MOEAFramework.src.org.moeaframework.Instrumenter.instrument.Extraction1.java
- MOEAFramework.src.org.moeaframework.Instrumenter.instrument.Extraction2.java


will produce the file `MOEAFramework.src.org.moeaframework.Instrumenter.instrument.json` with the results.

## Re-run specific methods only
If a json file with the results for a method exists, the script will not produce a new result. You need to remove/rename the file to get a new one. Thus, only the removed/renames one will be regenerated. (This is useful if you change the prompt to send to the model REST API).

## text-davinci-003 prompt
In the file open_api_model.py there are multiple versions of the prompt that can be sent. If a new one is needed just add it to the file, the predictions to codex can be called specifying the explicit version or changing the default in the calls to codex.


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
