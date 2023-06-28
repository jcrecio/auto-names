import json
import time
from objects import DatasetItem
import re

import spacy
nlp = spacy.load('en_core_web_md')

def log(message):
    print('[' + time.strftime("%H:%M:%S", time.localtime()) + ']: ' + message)

def error(message):
    print('ERROR [' + time.strftime("%H:%M:%S", time.localtime()) + ']: ' + message)

def read_file(file_path):
    with open(file_path, 'r') as file:
        file_content = file.read()
    return file_content

def read_file_as_json(file_path):
    with open(file_path, 'r') as file:
        content = json.load(file)
    return content
     
def get_dataset_item(file_name, code):
    file_name_parts = file_name.split('.')
    variation = file_name_parts[-2]
    method_name = file_name_parts[-3]
    class_name = file_name_parts[-4]
    project = file_name_parts[0:-4]
    return DatasetItem(project='.'.join(project), 
                class_name=class_name,
                method_name=method_name,
                variation=variation,
                code=code)

def write_file(file_path, content):
    file = open(file_path, 'w')
    file.write(content)
    file.close()

def extract_method_tokens(input):
    pattern = r'(?<=[a-z])(?=[A-Z])'
    return re.split(pattern, input)
    
def get_phrase_from_method(method_name):
    words = re.findall('[a-z]+|[A-Z][a-z]*', method_name)
    s = ' '.join(word.lower() for word in words)
    return s[0].capitalize() + s[1:]

def compute_method_similarity(method_name1, method_name2):
    phrase1 = get_phrase_from_method(method_name1)
    phrase2 = get_phrase_from_method(method_name2)
    doc1 = nlp(phrase1)
    doc2 = nlp(phrase2)

    return doc1.similarity(doc2)