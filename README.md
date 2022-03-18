# DecisionTreeJava
This project presents a Recursive Implementation of a Decision Tree in Java. The Decision Tree is based on the data-set attached in the data subfolder that presents labelled dataset with 6 attributes: 

- price - String
- maint - String
- doors - Integer
- persons - Integer
- lug_boot - String
- Safety - String

The label for each data point is a String which is of the format - acceptable, good, vgood, etc. 

The file expects data to be presented in the format for a csv file, whose path can be entered as a final variable in the code itself. An example acceptable csv entry is: 

```
  vhigh,vhigh,2,2,small,low,unacc
```

Each attribute is in the same order as specified in the list above. 

The code consists of several functions commented and labelled accordingly. The ```main()``` function is responsible for reading data, creating the decision tree recursively and printing it in a spatially separated order if necessary. A test file can also be introduced to test the accuracy of the decision tree once it's constructed. 
