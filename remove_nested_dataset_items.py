# Script used to remove nested methods from Archive_0 to produce Archive_0_no_nested
import os

# Load projects...
projects = list()

nested_files = list()
for project_key, project in projects.items():
        for class_key, class_name in project.classes.items():
            for method_key, method in class_name.methods.items():
                extraction_index = 1
                all_good = True
                for extraction in method.extractions:
                        if (all_good == False):
                            nested_files.append(extraction.file_name)
                        if (extraction.code.count(method_key + "_extraction_") != extraction_index):
                            all_good = False
                        extraction_index += 1
                if (all_good == False): nested_files.append(extraction.file_name)

nested_files = list(set(nested_files))
files_to_remove = list()
for file in nested_files:
    file_without_end_array = file.rsplit(".", 2)[:-2]
    file_without_end = ".".join(file_without_end_array)
    extractions_to_remove = [file for file in os.listdir('datasets/Archive_0_no_nested') if file.startswith(file_without_end + ".Extraction")]
    files_to_remove.extend(extractions_to_remove)
    files_to_remove.append(file_without_end + ".Original.java")

for file in files_to_remove:
    file_path = os.getcwd() + os.path.join('\\datasets\\Archive_0_no_nested', file)
    file_path = file_path.replace('\\\\','\\')
    if (os.path.exists(file_path)): os.remove(file_path)