/**
 This class defines a decision tree based on the attribute values presented in the file path specified
 This Decision Tree is specifically suited for a data-set that provides the following attributes:
 1. Price
 2. Maintenance
 3. Doors
 4. Persons
 5. Lug_Boot
 6. Safety

 and provides the corresponding final class label, which defines how we feel about the car (class label):

 1. Unacceptable
 2. Acceptable
 3. Good
 4. Very Good.

 The final decision tree can be generated and allows for tree-printing in a space separated format.
 The Decision Tree relies on choosing the majority label in cases where votes are divided. Ties are not broken and
 all possible majority vote labels are printed when the tree is printed.

 @author Anupam Gupta
 Running Instructions: Only the Main function needs to be run with no special flags
 */

import java.io.File;
import java.io.IOException;
import java.util.*;
public class DecisionTree {

    // Variable name that defines the file path from where to read the data
    private static final String FILE_PATH = <file_path>;

    // Attribute Names
    private static final String LABEL_ATTRIBUTE_NAME  = "labelValues";
    private static final String PRICE_ATTRIBUTE_NAME = "price";
    private static final String MAINTENANCE_ATTRIBUTE_NAME = "maint";
    private static final String DOORS_ATTRIBUTE_NAME = "doors";
    private static final String PERSONS_ATTRIBUTE_NAME = "persons";
    private static final String LUG_BOOT_ATTRIBUTE_NAME = "lug_boot";
    private static final String SAFETY_ATTRIBUTE_NAME = "safety";

    /**
     * Private Class that stores a Tree Edge. A Tree Edge contains
     * 1. The attribute value for the edge
     * 2. A Map containing the following mapping: label-values -> number of data-points with that label-value
     * 3. TreeNode that the edge connects to
     *
     * Getters/Setters are defined as required.
     */
    private static class TreeEdge {
        String edgeValue;
        Map<String, Integer> labelValues;
        TreeNode child;

        TreeEdge(String edgeValues, Map<String, Integer> labelValues, TreeNode child) {
            this.edgeValue = edgeValues;
            this.labelValues = labelValues;
            this.child = child;
        }

        public String getEdgeValue() {
            return this.edgeValue;
        }

        public Map<String, Integer> getLabelValues() {
            return this.labelValues;
        }

        public TreeNode getChild() {
            return this.child;
        }

        public void setChild(TreeNode child) {
            this.child = child;
        }

    }

    /**
     * Private Class that defines a Decision TreeNode. A Decision TreeNode contains:
     * 1. Attribute Value for the tree node
     * 2. Attribute Name  - for example price, maintenance, doors, etc
     * 3. List of Tree Edges for this TreeNode
     *
     * Getters/Setters are defined as required.
     */
    private static class TreeNode {
        String nodeValue;
        String attributeName;
        List<TreeEdge> children;

        TreeNode(String nodeValue, String typeOfValue, List<TreeEdge> children) {
            this.nodeValue = nodeValue;
            this.children = children;
            this.attributeName = typeOfValue;
        }

        public String getNodeValue() {
            return this.nodeValue;
        }

        public List<TreeEdge> getChildren() {
            return this.children;
        }

        public String getAttributeName() {
            return this.attributeName;
        }
    }

    /**
     * main method that imports data, constructs the tree and prints the final tree in a space separated format.
     * @param args - general args, not used in method
     * @throws IOException in case file specified in FILE_PATH is not found
     */
    public static void main(String[] args) throws IOException {
        Map<String, List<String>> mapOfValues = import_data();
        TreeNode rootNode = chooseNode(mapOfValues);
        rootNode = constructTree(rootNode, mapOfValues);
        printTree(0, rootNode);
    }

    /**
     * Private Function to print the entire Tree recursively in a spatially separated manner
     * NOTE: In cases where the final tree decision remains undecided i.e. multiple equally probable options are
     * possible, all such possibilities are printed separated by a slash.
     * @param numberOfTabs Number of Tabs to print before printing the current values
     * @param root The current Tree node whose value we are printing
     */
    private static void printTree(int numberOfTabs, TreeNode root) {
        for(int j = 0; j < numberOfTabs; j++) {
            System.out.print("\t");
        }

        System.out.println(root.getNodeValue());
        for(int i = 0; i < root.getChildren().size(); i++) {
            for(int j = 0; j < numberOfTabs; j++) {
                System.out.print("\t");
            }
            System.out.println(" | " + root.getAttributeName() + " = " + root.getChildren().get(i).getEdgeValue() + " --> ");
            boolean isNull = false;
            if(root.getChildren().get(i).getChild() == null) {
                int majorityVote = 0;
                String majorityVoteString = "";
                for(Map.Entry<String, Integer> entry: root.getChildren().get(i).getLabelValues().entrySet()) {
                    if(entry.getValue() > majorityVote) {
                        majorityVote = entry.getValue();
                        majorityVoteString = entry.getKey();
                    }
                }
                List<String> possibleMajorityValues = new ArrayList<>(Collections.singletonList(majorityVoteString));
                for(Map.Entry<String, Integer> entry: root.getChildren().get(i).getLabelValues().entrySet()) {
                    if(entry.getValue() ==  majorityVote && !entry.getKey().equals(majorityVoteString)) {
                        possibleMajorityValues.add(entry.getKey());
                    }
                }
                for(int j = 0; j < numberOfTabs + 1; j++) {
                    System.out.print("\t");
                }
                if(possibleMajorityValues.size() == 1) {
                    System.out.println(majorityVoteString);
                } else {
                    String majorityString = "";
                    for(int m = 0; m < possibleMajorityValues.size() - 1; m++) {
                        majorityString += possibleMajorityValues.get(m) + "/";
                    }
                    majorityString += possibleMajorityValues.get(possibleMajorityValues.size()-1);
                    System.out.println(majorityString);
                }
                isNull = true;
            }
            if(isNull) {
                continue;
            }
            printTree(numberOfTabs + 1, root.getChildren().get(i).getChild());
        }
    }

    /**
     * Private method to import data from the file path specified
     * Reads data in the format:
     *
     * price,maint,doors,persons,lug_boot,safety,label
     *
     * @return Map of globally defined attribute names to ArrayLists of sequential values read from file
     * @throws IOException when file is not found at file_path
     */
    private static Map<String, List<String>> import_data() throws IOException{
        List<String> price = new ArrayList<String>();
        List<String> maint = new ArrayList<String>();
        List<String> doors = new ArrayList<String>();
        List<String> persons = new ArrayList<String>();
        List<String> lug_boot = new ArrayList<String>();
        List<String> safety = new ArrayList<String>();
        List<String> label = new ArrayList<String>();

        Map<String, List<String>> mapOfValues = new HashMap<String, List<String>>();
        mapOfValues.put(PRICE_ATTRIBUTE_NAME, price);
        mapOfValues.put(MAINTENANCE_ATTRIBUTE_NAME, maint);
        mapOfValues.put(DOORS_ATTRIBUTE_NAME, doors);
        mapOfValues.put(PERSONS_ATTRIBUTE_NAME, persons);
        mapOfValues.put(LUG_BOOT_ATTRIBUTE_NAME, lug_boot);
        mapOfValues.put(SAFETY_ATTRIBUTE_NAME, safety);
        mapOfValues.put(LABEL_ATTRIBUTE_NAME, label);

        Scanner sc = new Scanner(new File(FILE_PATH));
        sc.useDelimiter(",|\\n");
        while (sc.hasNext()) {
            price.add(sc.next());
            maint.add(sc.next());
            doors.add(sc.next());
            persons.add(sc.next());
            lug_boot.add(sc.next());
            safety.add(sc.next());
            label.add(sc.next());
        }
        return mapOfValues;
    }

    /**
     * Helper function to get distinct values from a list of strings
     * @param listOfElements list to get distinct values from
     * @return Un-ordered Set of Distinct Values present in input list
     */
    private static Set<String> generateDistinctSets(List<String> listOfElements) {
        return new HashSet<>(listOfElements);
    }

    /**
     * Private Function to compute the Entropy of a certain collection of label values
     * @param numberOfValues Map of label-name to number of data-points with that label
     * @param <T> Generic to stand in-place for the label-name (usually string)
     * @return the Entropy calculated as addition of -p * log2p across all label values
     * where p is the fraction of data-points that hold that specific label value.
     */
    private static <T> double computeEntropy(Map<T, Integer> numberOfValues) {
        double entropy = 0.0;
        int totalNumberOfValues = 0;
        for(Integer numberOfEntries: numberOfValues.values()) {
            totalNumberOfValues += numberOfEntries;
        }

        for(Integer numberOfEntries: numberOfValues.values()) {
            double p = ((numberOfEntries * 1.0)/(totalNumberOfValues * 1.0));
            entropy += -1.0 * p * (Math.log(p)/Math.log(2.0));;
        }

        return entropy;
    }

    /**
     * Private Function to Recursively Generate The Decision Tree in a Depth First Manner
     * @param root The current root node to start constructing the tree from
     * @param availableAttributes Map of attribute_name to list of possible values for that attribute
     * @return RootNode of a tree with entire sub-tree beneath it constructed
     */
    private static TreeNode constructTree(TreeNode root, Map<String, List<String>> availableAttributes) {
        if(root == null || availableAttributes == null || availableAttributes.isEmpty() || availableAttributes.size() == 2) {
            return null;
        }
        List<String> listOfValuesForThisRootNode = availableAttributes.get(root.getNodeValue());
        List<String> distinctValuesOfRoot = new ArrayList<String>(generateDistinctSets(listOfValuesForThisRootNode));

        for(int i = 0; i < distinctValuesOfRoot.size(); i++) {
            Map<String, List<String>> computedHashMap = new HashMap<String, List<String>>();
            for (Map.Entry<String, List<String>> entry : availableAttributes.entrySet()) {
                if (entry.getKey().equals(root.getNodeValue())) {
                    continue;
                }
                computedHashMap.put(entry.getKey(), new ArrayList<>());
            }

            for (int j = 0; j < listOfValuesForThisRootNode.size(); j++) {
                if (listOfValuesForThisRootNode.get(j).equals(distinctValuesOfRoot.get(i))) {
                    for (Map.Entry<String, List<String>> entry : computedHashMap.entrySet()) {
                        String attributeName = entry.getKey();
                        List<String> attributeValues = computedHashMap.get(attributeName);
                        attributeValues.add(availableAttributes.get(attributeName).get(j));
                        computedHashMap.put(attributeName, attributeValues);
                    }
                }
            }

            int index = 0;
            for(int l = 0; l < root.getChildren().size(); l++) {
                if(root.getChildren().get(l).getEdgeValue().equals(distinctValuesOfRoot.get(i))) {
                    index = l;
                    break;
                }
            }
            root.getChildren().get(index).setChild(constructTree(chooseNode(computedHashMap), computedHashMap));
        }
        return root;
    }

    /**
     * Private Function to choose the attribute value to split on and create a Tree Node around it.
     * The decision on which attribute value should be chosen is based on the attribute that provides the maximum
     * information gain. Information Gain is defined as the difference between the entropies pre and post split.
     *
     * @param availableNodes Map of Attribute Values and the possible values the attribute can take.
     *                       NOTE: There should always be an attribute with the @LABEL_ATTRIBUTE_NAME
     *                       attribute name present in the map. This contains the sequential label mapping for all
     *                       the data points
     * @return Tree node who's value is the attribute name to split on and which contains tree edges for each
     * of it's possible values. Each such tree edge is initially just connected to a null Tree Node.
     */
    private static TreeNode chooseNode(Map<String, List<String>> availableNodes) {
        double maxIG = 0.0;
        String maxIGString = "";
        String maxIGAttributeName = "";
        List<String> values = availableNodes.get(LABEL_ATTRIBUTE_NAME);
        Map<Object, Integer> numberOfValues = new HashMap<>();

        for(Object value: values) {
            if (!numberOfValues.containsKey(value)) {
                numberOfValues.put(value, 0);
            }
            numberOfValues.put(value, numberOfValues.get(value) + 1);
        }

        double originalEntropy = computeEntropy(numberOfValues);

        for(Map.Entry<String, List<String>> entry: availableNodes.entrySet()) {
            if(entry.getKey().equals(LABEL_ATTRIBUTE_NAME)) continue;

            String key = entry.getKey();
            List<String> valueList = entry.getValue();
            Set<String> distinctValues = generateDistinctSets(valueList);

            double entropyLocal = 0;
            for(Object value: distinctValues) {
                Map<Object, Integer> numberOfEntries = new HashMap<>();
                int count = 0;
                for (int i = 0; i < valueList.size(); i++) {
                    if (valueList.get(i).equals(value)) {
                        count++;
                        if (!numberOfEntries.containsKey(values.get(i))) {
                            numberOfEntries.put(values.get(i), 0);
                        }
                        numberOfEntries.put(values.get(i), numberOfEntries.get(values.get(i)) + 1);
                    }
                }
                double entropyLocalForValue = ((count * 1.0)/ (entry.getValue().size() * 1.0) * computeEntropy(numberOfEntries));
                entropyLocal += entropyLocalForValue;
            }

            double informationGainFromThisSplit = originalEntropy - entropyLocal;

            if(informationGainFromThisSplit > maxIG) {
                maxIG = informationGainFromThisSplit;
                maxIGString = key;
                maxIGAttributeName = entry.getKey();
            }
        }

        if(maxIG == 0.0) {
            return null;
        }

        // Compute tree edges
        List<String> valuesOfTopNode = availableNodes.get(maxIGString);
        Set<String> distinctValuesOfTopNode = generateDistinctSets(valuesOfTopNode);
        List<TreeEdge> listOfTreeEdges = new ArrayList<TreeEdge>();
        distinctValuesOfTopNode.stream().forEach(distinctValue -> {
            Map<String, Integer> storageMap = new HashMap<String, Integer>();
            for(int i = 0; i < valuesOfTopNode.size(); i++) {
                if(valuesOfTopNode.get(i).equals(distinctValue)) {
                    if(!storageMap.containsKey(values.get(i))) {
                        storageMap.put(values.get(i), 0);
                    }
                    storageMap.put(values.get(i), storageMap.get(values.get(i)) + 1);
                }
            }
            TreeEdge edge = new TreeEdge(distinctValue, storageMap, null);
            listOfTreeEdges.add(edge);
        });
        return new TreeNode(maxIGString, maxIGAttributeName, listOfTreeEdges);
    }
}
