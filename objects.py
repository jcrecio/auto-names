class DatasetItem:
    def __init__(self, project, class_name, method_name, variation, code):
        self.project = project
        self.class_name = class_name
        self.method_name = method_name
        self.variation = variation
        self.code = code


class Project:
    def __init__(self):
        self.classes = dict()


class ClassObject:
    def __init__(self):
        self.methods = dict()


class Method:
    def __init__(self):
        self.original = None
        self.extractions = list()
        self.file_name = None

    def set_original(self, original):
        self.original = original

    def add_extraction(self, extraction):
        self.extractions.append(extraction)


class OriginalMethodContent:
    def __init__(self, method_name, code, file_name, extraction_name=None):
        self.method_name = method_name
        self.code = code
        self.extraction_name = extraction_name
        self.predictions = list()
        self.similarities = list()
        self.file_name = file_name


class ExtractionMethodContent:
    def __init__(self, method_name, code, file_name, extraction_name=None):
        self.method_name = method_name
        self.code = code
        self.extraction_name = extraction_name
        self.prediction = None
        self.similarity = None
        self.file_name = file_name

        self.updated_code = None


class Error:
    def __init__(self, error_description, method_name=None):
        self.method_name = method_name
        self.error_description = error_description


def serialize(obj):
    if isinstance(obj, Project):
        return obj.classes
    elif isinstance(obj, ClassObject):
        return obj.methods
    elif isinstance(obj, Error):
        return {
            "error_description": obj.error_description,
            "method_name": obj.method_name,
        }
    else:
        return obj.__dict__
