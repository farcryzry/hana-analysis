package hana.analysis.api;

import hana.analysis.models.Analysis;
import hana.analysis.models.AnalysisResult;
import hana.analysis.models.DataSource;
import hana.analysis.models.KmeansAlgorithm;
import hana.analysis.models.NbAlgorithm;

import java.util.*;

public class Pal {
	/**
	 * <pre>
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P32
	 * </pre>
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': 	Number of threads.
	 * 'GROUP_NUMBER': 	Number of groups (k)
	 * 'INIT_TYPE':    	Center initialization type:
	 * 					1 = first K 
	 * 					2 = random with replacement 
	 * 					3 = random without replacement 
	 * 					4 (default) = one patent of selecting the init center (US 6,882,998 B1)
	 * 
	 * 'DISTANCE_LEVEL':	Computes the distance between the item and the cluster center.
	 * 					 1 = Manhattan distance
	 * 					 2 (default) = Euclidean distance
	 * 					 3 = Minkowski distance
	 * 					 4 = Chebyshev distance
	 * 					
	 * 'MAX_ITERATION':	Maximum iterations. The default is 100
	 * 'NORMALIZATION':	Normalization type:
	 * 					 0 (default) = no
	 * 					 1 = yes.
	 * 					 2 = for each column C
	 * 					
	 * 'EXIT_THRESHOLD':	Threshold (actual value) for exiting the iterations. The default is 0.000000001
	 * </pre>
	 * 
	 * @return KM_RESULTS, KM_CENTERS
	 */
	public static AnalysisResult kmeans(boolean reGenerate, String schemaName, String tableData, LinkedHashMap<String, String> columns, String viewDef,
			LinkedHashMap<String, Object> params) {

		Analysis analysis = new Analysis(new KmeansAlgorithm(), schemaName);

		DataSource source = new DataSource(schemaName, tableData, columns,viewDef);

		AnalysisResult result = analysis.action(reGenerate, source, params);
		System.out.println(result);
		return result;
	}
	
	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P109
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'IS_SPLIT_MODEL': Indicates whether to split the string of the model.
	 * 				 0: does not split the model
	 * 				 Other value: splits the model. The maximum length of each unit is 5000.
	 * 'LAPLACE': Enables or disables Laplace smoothing.
	 * 				 0 (default): disables Laplace smoothing
	 * 				 Positive value: enables Laplace smoothing for discrete values
	 * </pre>
	 * 
	 * @return NB_MODEL
	 */
	public static AnalysisResult naiveBayes(boolean reGenerate, String schemaName, String tableData, LinkedHashMap<String, String> columns, String viewDef,
			LinkedHashMap<String, Object> params) {
		Analysis analysis = new Analysis(new NbAlgorithm(), schemaName);

		DataSource source = new DataSource(schemaName, tableData, columns, viewDef);

		AnalysisResult result = analysis.action(reGenerate, source, params);
		System.out.println(result);
		return result;
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P109
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param tableNBModel
	 *            Table name for output data
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'IS_SPLIT_MODEL': Indicates whether to split the string of the model.
	 * 				 0: does not split the model
	 * 				 Other value: splits the model. The maximum length of each unit is 5000.
	 * 'LAPLACE': Enables or disables Laplace smoothing.
	 * 				 0 (default): disables Laplace smoothing
	 * 				 Positive value: enables Laplace smoothing for discrete values
	 * </pre>
	 * 
	 * @return NBP_PREDICT
	 */
	public String naiveBayesPredict(String tableData, String[] columns,
			String tableNBModel, LinkedHashMap<String, Object> params) {
		// TODO
		return "NBP_PREDICT";
	}

	/**
	 * <pre>
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P43
	 * </pre>
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'SIZE_OF_MAP': ???
	 * 'HEIGHT_OF_MAP': Indicates the height of the map. The default is 10.
	 * 'WIDTH_OF_MAP': Indicates the width of the map. The default is 10.
	 * 'MAX_ITERATION': Maximum number of iterations. The default is 200.
	 * 'NORMALIZATION': Normalization type:
	 * 					 0 (default) = no
	 * 					 1 = transform to new range (0.0, 1.0)
	 * 					 2 = z-score normalization
	 * </pre>
	 * 
	 * @return SOM_RESULTS, SOM_MAP
	 */
	public String[] selfOrganizationMap(String tableData, String[] columns,
			LinkedHashMap<String, Object> params) {
		// TODO
		return new String[] { "SOM_RESULTS", "SOM_MAP" };
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P234
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param tableMapValues
	 *            Table name for output data
	 * @param tableMapVars
	 *            Table name for output data
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 
	 * @return WT_RESULTS
	 */
	public String weightedScoreTable(String tableData, String[] columns,
			String tableMapValues, String tableMapVars,
			LinkedHashMap<String, Object> params) {
		// TODO
		return "WT_RESULTS";
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P88
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param tableKNNClass
	 *            Table name for input data
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'ATTRIBUTE_NUM': Number of attributes. Default value: 1
	 * 'K_NEAREST_NEIGHBOURS': Number of nearest neighbors (k). Default value: 1
	 * 'VOTING_TYPE': Voting type:
	 * 	 0 = majority voting
	 * 	 1 (default) = distance-weighted voting
	 * </pre>
	 * 
	 * @return KNN_PREDICT
	 */
	public String kNearestNeighbor(String tableKNNData, String[] columns,
			String tableKNNClass, LinkedHashMap<String, Object> params) {
		// TODO
		return "KNN_PREDICT";
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P92
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'MAX_ITERATION': Maximum number of iterations. Default value: 1
	 * 'EXIT_THRESHOLD': Threshold (actual value) for exiting the iterations. Default value: 0.000001
	 * 'VARIABLE_NUM': (Optional) Specifies the number of independent variables (Xi). Default value: all Xi columns in DataTab
	 * 'METHOD':
	 * 	 0 (default and recommended): uses the Newton iteration method.
	 * 	 1: uses the gradient-decent method.
	 * 	 2: uses the L1 regularized Newton iteration method.
	 * 	 3: uses the BFGS method.
	 * 'PMML_EXPORT':
	 * 	 0 (default): does not export logistic regression model in PMML.
	 * 	 1: exports logistic regression model in PMML in single row.
	 * 	 2: exports logistic regression model in PMML in several rows, and the minimum length of each row is 5000 characters.
	 * </pre>
	 * 
	 * @return RG_COEFF, RG_PMML
	 */
	public String[] logisticRegression(String tableData, String[] columns,
			LinkedHashMap<String, Object> params) {
		// TODO
		return new String[] { "RG_COEFF", "RG_PMML" };
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P92
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param tableCoeff
	 *            Table name for output data
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'MAX_ITERATION': Maximum number of iterations. Default value: 1
	 * 'EXIT_THRESHOLD': Threshold (actual value) for exiting the iterations. Default value: 0.000001
	 * 'VARIABLE_NUM': (Optional) Specifies the number of independent variables (Xi). Default value: all Xi columns in DataTab
	 * 'METHOD':
	 * 	 0 (default and recommended): uses the Newton iteration method.
	 * 	 1: uses the gradient-decent method.
	 * 	 2: uses the L1 regularized Newton iteration method.
	 * 	 3: uses the BFGS method.
	 * 'PMML_EXPORT':
	 * 	 0 (default): does not export logistic regression model in PMML.
	 * 	 1: exports logistic regression model in PMML in single row.
	 * 	 2: exports logistic regression model in PMML in several rows, and the minimum length of each row is 5000 characters.
	 * </pre>
	 * 
	 * @return RGP_PREDICTED
	 */
	public String logisticRegressionPredict(String tableData, String[] columns,
			String tableCoeff, LinkedHashMap<String, Object> params) {
		// TODO
		return "RGP_PREDICTED";
	}

	

	/**
	 * Decision Tree C4.5 SAP_HANA_Predictive_Analysis_Library_PAL_en P66
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'PERCENTAGE': Specifies the percentage of the input data to be used to build the tree model. For example, if you set this parameter to 0.7, 70% of the training data will be used to build the tree model, and 30% will be used to prune the tree model. Default: 1.0
	 * 'MIN_NUMS_RECORDS': Specifies the stop condition: if the number of records is less than the parameter value, the algorithm will stop splitting. Default: 0
	 * 'IS_SPLIT_MODEL': Indicates whether the string of the tree model should be split or not.If the value is not 0, the tree model will be split, and the length of each unit is 5000. Default: 0
	 * 'PMML_EXPORT': 
	 * 	 0 (default): does not export PMML tree model.
	 * 	 1: exports PMML tree model in single row.
	 * 	 2: exports PMML tree model in several rows, and the minimum length of each row is 5000 characters. Note: This parameter is valid only when the IS_OUTPUT_RULES parameter is set to 0.
	 * 'CONTINUOUS_COL':  Indicates which column contains continuous variables:
	 * 	 Integer value specifies the column position (column index starts from zero)
	 * 	 Double value specifies the interval. If this value is not specified, the algorithm will automatically split this continuous value.
	 * 	Default:
	 * 	 String or integer is discrete attribute
	 * 	 Double is continuous attribute
	 * </pre>
	 * 
	 * @return DT_MODEL_JSON, DT_MODEL_PMML
	 */
	public String[] decisionTree(String tableData, String[] columns,
			LinkedHashMap<String, Object> params) {
		// TODO
		return new String[] { "DT_MODEL_JSON", "DT_MODEL_PMML" };
	}

	/**
	 * Decision Tree C4.5 SAP_HANA_Predictive_Analysis_Library_PAL_en P66
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param tableModleJson
	 *            Table name for output data
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'PERCENTAGE': Specifies the percentage of the input data to be used to build the tree model. For example, if you set this parameter to 0.7, 70% of the training data will be used to build the tree model, and 30% will be used to prune the tree model. Default: 1.0
	 * 'MIN_NUMS_RECORDS': Specifies the stop condition: if the number of records is less than the parameter value, the algorithm will stop splitting. Default: 0
	 * 'IS_SPLIT_MODEL': Indicates whether the string of the tree model should be split or not.If the value is not 0, the tree model will be split, and the length of each unit is 5000. Default: 0
	 * 'PMML_EXPORT': 
	 * 	 0 (default): does not export PMML tree model.
	 * 	 1: exports PMML tree model in single row.
	 * 	 2: exports PMML tree model in several rows, and the minimum length of each row is 5000 characters. Note: This parameter is valid only when the IS_OUTPUT_RULES parameter is set to 0.
	 * 'CONTINUOUS_COL':  Indicates which column contains continuous variables:
	 * 	 Integer value specifies the column position (column index starts from zero)
	 * 	 Double value specifies the interval. If this value is not specified, the algorithm will automatically split this continuous value.
	 * 	Default:
	 * 	 String or integer is discrete attribute
	 * 	 Double is continuous attribute
	 * </pre>
	 * 
	 * @return DTP_PREDICT
	 */
	public String decisionTreePredit(String tableData, String[] columns,
			String tableModleJson, LinkedHashMap<String, Object> params) {
		// TODO
		return "DTP_PREDICT";
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P123
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'KERNEL_TYPE': Kernel type:
	 * 		 0: LINEAR KERNEL
	 * 		 1: POLY KERNEL
	 * 		 2: RBF KERNEL
	 * 		 3: SIGMOID KERNEL
	 * 		Default value: 2
	 * 'TYPE': SVM type:
	 * 		 1: SVC (for classification)
	 * 		 2: SVR (for regression)
	 * 		 3: Support Vector Ranking (for ranking)
	 * </pre>
	 * 
	 * @return SV_MODEL1, SV_MODEL2
	 */
	public String[] supportVectorMachine(String tableData, String[] columns,
			LinkedHashMap<String, Object> params) {
		// TODO
		return new String[] { "SV_MODEL1", "SV_MODEL2" };
	}

	/**
	 * SAP_HANA_Predictive_Analysis_Library_PAL_en P123
	 * 
	 * @param tableData
	 *            Table name for input data
	 * @param columns
	 *            Column names for the input data table
	 * @param tableSVModel1
	 *            Table name for output data
	 * @param tableSVModel2
	 *            Table name for output data
	 * @param params
	 *            Parameters for the algorithm
	 * 
	 *            <pre>
	 * 'THREAD_NUMBER': Number of threads.
	 * 'KERNEL_TYPE': Kernel type:
	 * 		 0: LINEAR KERNEL
	 * 		 1: POLY KERNEL
	 * 		 2: RBF KERNEL
	 * 		 3: SIGMOID KERNEL
	 * 		Default value: 2
	 * 'TYPE': SVM type:
	 * 		 1: SVC (for classification)
	 * 		 2: SVR (for regression)
	 * 		 3: Support Vector Ranking (for ranking)
	 * </pre>
	 * 
	 * @return SVP_PREDICT
	 */
	public String supportVectorMachinePredict(String tableData,
			String[] columns, String tableSVModel1, String tableSVModel2,
			LinkedHashMap<String, Object> params) {
		// TODO
		return "SVP_PREDICT";
	}
}
