import matplotlib.pyplot as plt
import numpy as np
import json
import os
from utils import read_file_as_json

import matplotlib.pyplot as plt
import numpy as np

method_similarity = {
    1: [],
    2: [],
    3: [],
    4: [],
    5: [],
    6: [],
    7: []
}

similarities = {
    1: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0},
    2: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0},
    3: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0},
    4: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0},
    5: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0},
    6: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0},
    7: {'count': 0, 'total': 0, 'avg': 0, 'under50': 0, 'over50': 0}
}

methods_with_dashes = []
multiple_methods = []

for file_name in os.listdir('output'):
    file = read_file_as_json('output/' + file_name)

    if 'original' not in file:
        continue
    if 'predictions' not in file['original']:
        continue
    if (len(file['original']['predictions']) > 0):
        for p in file['original']['predictions']:
            if ('_' in p):
                methods_with_dashes.append(
                    {'name': file['original']['method_name'], 'predictions': file['original']['predictions']
                     })
            if (',' in p):
                multiple_methods.append(
                    {'name': file['original']['method_name'], 'predictions': file['original']['predictions']
                     })

    if 'similarities' not in file['original']:
        continue

    file_similarities = file['original']['similarities']
    if (len(similarities) <= 0):
        continue

    for i in range(len(file_similarities)):
        similarity_entry = similarities[i+1]
        method_similarity_entry = method_similarity[i+1]
        method_similarity_entry.append({'name': file['original']['method_name'], 
                                        'similarity': file_similarities[i]})
        similarity_entry['count'] += 1
        similarity_entry['total'] += file_similarities[i]
        if (file_similarities[i] >= 0.5): similarity_entry['over50'] += 1
        elif (file_similarities[i] < 0.5): similarity_entry['under50'] += 1

for s in similarities:
    if (similarities[s]['count'] == 0):
        continue
    similarities[s]['avg'] = similarities[s]['total'] / \
        similarities[s]['count']

print('Similarities:')
print(json.dumps(similarities, indent=4))

print('Multiple predictions: (' + str(len(multiple_methods)) + ')')
print(json.dumps(multiple_methods, indent=4))

print('Method with dashes: (' + str(len(methods_with_dashes)) + ')')
print(json.dumps(methods_with_dashes, indent=4))

print('Method with similarities: (' + str(len(method_similarity)) + ')')
print(json.dumps(method_similarity, indent=4))

def display_method_similarities_after_extractions(extractions):
    method_similarities = sorted(method_similarity[extractions], key = lambda x: x['similarity'])
    method_names = [x['name'] for x in method_similarities]
    x_method_names = np.arange(len(method_names))
    y_similarities = [x['similarity'] for x in method_similarities]

    extractions_word = ""
    if (extractions == 1): extractions_word = "one"
    elif (extractions == 2): extractions_word = "two"
    elif (extractions == 3): extractions_word = "three"
    elif (extractions == 4): extractions_word = "four"
    elif (extractions == 5): extractions_word = "five"
    elif (extractions == 6): extractions_word = "six"

    plt.figure(figsize=(11, 7))

    plt.bar(x_method_names, y_similarities)
    plt.xlabel('Methods')
    plt.ylabel('Similarity after ' + extractions_word + ' extractions')
    plt.title('Similarity Scores')

    plt.xticks(x_method_names, method_names, rotation='vertical', fontsize=8)

    percentiles = [25, 50, 75]  # Percentile values
    percentiles_name = ['Q1', 'Q2', 'Q3']  # Percentile values
    for i in range(3):
        value = np.percentile(y_similarities, percentiles[i])  # Calculate the percentile value
        plt.text(0, value, percentiles_name[i], color='red', ha='right', va='bottom')  # Add text to the line
        plt.axhline(value, color='red', linestyle='--', linewidth=1)  # Add a horizontal line

    plt.tight_layout() 

    plt.show(block=True)

display_method_similarities_after_extractions(1)
display_method_similarities_after_extractions(2)
display_method_similarities_after_extractions(3)
display_method_similarities_after_extractions(4)
