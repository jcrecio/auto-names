/** 
 * Get the matrix that represent the data
 * @param columnsSpecification: A list with all the possible coordinates of the columns
 * @param rowsSpecification:    A list with all the possible coordinates of the rows
 * @param columnCordinates:     A list with the column coordinates of all the data
 * @param rowCordinates:        A list with the column rows of all the data
 * @param data:                 A list with the data
 * @param measuresOnColumns:    true if the measures live in the columns, false if the measures live in the rows
 * @param measuresLength:       the number of the measures
 * @return the matrix that represent the data
 */
private String[][] getDataMatrix(List<String> columnsSpecification,List<String> rowsSpecification,List<String> columnCordinates,List<String> rowCordinates,List<String> data,boolean measuresOnColumns,int measuresLength,int columnsN){
  String[][] dataMatrix;
  int x, y;
  int rowsN;
  if (measuresOnColumns) {
    rowsN=(rowsSpecification.size() > 0 ? rowsSpecification.size() : 1);
  }
 else {
    rowsN=(rowsSpecification.size() > 0 ? rowsSpecification.size() : 1) * measuresLength;
  }
  dataMatrix=new String[rowsN][columnsN];
  for (int i=0; i < rowsN; i++) {
    for (int j=0; j < columnsN; j++) {
      dataMatrix[i][j]=DATA_MATRIX_NA;
    }
  }
  if (measuresOnColumns) {
    for (int i=0; i < data.size(); i=i + measuresLength) {
      for (int j=0; j < measuresLength; j++) {
        y=getDataMatrix_extraction_2(columnsSpecification,rowsSpecification,columnCordinates,rowCordinates,data,measuresLength,columnsN,dataMatrix,y,i,j);
      }
    }
  }
 else {
    for (int i=0; i < data.size(); i=i + measuresLength) {
      for (int j=0; j < measuresLength; j++) {
        y=getDataMatrix_extraction_3(columnsSpecification,rowsSpecification,columnCordinates,rowCordinates,data,measuresLength,columnsN,dataMatrix,y,i,j);
      }
    }
  }
  return dataMatrix;
}
