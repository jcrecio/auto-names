import requests
import time
import os
from objects import Error

sleep_api = 0

predict_extraction_versions = {
    "v1": lambda x, y, z: """Given the initial java code: '{original_code}', and the following code: '{extraction_code}' which functionally is the same as the initial code, but having applied an extract refactor operation. Can you provide an appropriate meaningful method name for the extracted method '{method_to_compute}'? (Please provide the method name without parentheses)""".format(
        original_code=x, extraction_code=y, method_to_compute=z
    ),
    "v2": lambda x, y, z: """Given the initial java code: '{original_code}' and following code: '{extraction_code}' which functionally is the same as the initial code, but having applied an extract refactor operation. Can you give a meaningful name for the extracted method '{method_to_compute}'? (Please consider following points: 1 - Provide the method name without parenthesis. 2 - The keyword \'extraction\' means that the content of the method to predict was extracted from the initial code, so \'extracted\' should not be part of the method name.""".format(
        original_code=x, extraction_code=y, method_to_compute=z
    ),
    "v3": lambda x, y, z: """Given the initial java code: '{original_code}' and following code: '{extraction_code}' 
    which functionally is the same as the initial code, but having applied an extract refactor operation. 
    Can you give an meaningful name for the extracted method '{method_to_compute}'? 
    (Please consider following points: 1:Provide the method name without parentheses. 2:Do not use the keyword \'extraction\' means 
    that the content of the method to predict was extracted from the initial code, so \'extracted\' keyword should not be part of 
    method name. 3:Provide a valid method name.)""".format(
        original_code=x, extraction_code=y, method_to_compute=z
    ),
    "v4": lambda x, y, z: """Given the initial java code: '{original_code}' and its refactored version: '{extraction_code}' 
    Can you give a name for the extracted method '{method_to_compute}'?. Provide the method name without parentheses. Avoid the extract operation bias for the the method name prediction. Use java notation for the method name (for example, do not use _).
    """.format(
        original_code=x, extraction_code=y, method_to_compute=z
    ),
}

predict_original_versions = {
    "v1": lambda x: """Given the following java code, suggest a name for the method without taking into account the current method name and provide only the method name in the answer without any additional data, symbols or spaces: """
    + x,
}

completion_models = ["text-davinci-003"]
chat_completion_models = ["gpt-3.5-turbo", "gpt-4"]


class OpenAiModel:
    def __init__(self, model):
        self.model = model

    def create_request(self, prompt):
        if self.model in completion_models:
            return {"model": self.model, "prompt": prompt, "n": 1, "temperature": 0}
        elif self.model in chat_completion_models:
            # TODO jcrecio: Redesign calls to API to instruct the model using the system role to provide indications, etc
            return {
                "model": self.model,
                "messages": [{"role": "user", "content": prompt}],
            }

    def chat(self, prompt):
        try:
            json_request = self.create_request(prompt)
            response = requests.post(
                "https://api.openai.com/v1/chat/completions",
                headers={
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + os.getenv("OPEN_AI_KEY"),
                },
                json=json_request,
            )
            if response.status_code == 400:
                return Error(response.content)
            result = response.json()["choices"][0]["message"]["content"].strip()
            return result
        except Exception as e:
            return "error"

    def predict_extraction(
        self, original_code, extraction_code, method_to_compute, version="v4"
    ):
        prediction = self.chat(
            predict_extraction_versions[version](
                original_code, extraction_code, method_to_compute
            )
        )
        time.sleep(sleep_api)
        if isinstance(prediction, Error):
            return prediction
        return prediction.replace("()", "")

    def predict(self, input_code, method_name=None):
        if method_name == None:
            prediction = self.chat(
                """Given the following java code, suggest a name for the method without taking into account the current method name and provide only the method name in the answer without any additional data, symbols or spaces. (Do not provide more than one method name, and always use java standard and notation). This is the code: """
                + input_code
            )
            time.sleep(sleep_api)
            return prediction.replace("()", "")
        prediction = self.chat(
            """Given the following java code, suggest a name for the method : """
            + method_name
            + " without taking into account its current method name and provide only the method name in the answer without any additional data, symbols or spaces. (Do not provide more than one method name). This is the code: "
            + " code: "
            + input_code
        )
        time.sleep(sleep_api)
        return prediction.replace("()", "")
