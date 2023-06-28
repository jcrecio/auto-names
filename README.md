# Work and experiment
This code has been created to support the experiment described by the work *Naming Methods Automatically after Refactoring Operations*.


It makes use of the *text-davinci-003* model to make method names predictions.

## Setup
In order to run the experiment you will need an OpenAI API key to authorize the REST HTTP requests to the model, that key you will need to add it into the authorization header in file open_api_model.py

## Run the script
1. To run the script, just use the command: ```python run_experiment.py <folder with java codes to predict>```

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
