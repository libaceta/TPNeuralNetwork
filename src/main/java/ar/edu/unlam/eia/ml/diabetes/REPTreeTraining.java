package ar.edu.unlam.eia.ml.diabetes;

import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.unsupervised.attribute.Remove;

/**
 * Created by TigerShark on 8/3/2017.
 */
public class REPTreeTraining {

    public static void main(String[] args) throws Exception {

        // Levantamos el dataset "diabetes.csv"
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("datasets/diabetes.csv");

        // Obtenemos las instancias (juegos de datos)
        Instances train = source.getDataSet();

        // Seteamos cual atributo (columna) es la "clase" (si tiene o no diabetes)
        train.setClassIndex(train.numAttributes() - 1);

        // Obtenemos una copia de las instancias para testear el entrenamiento
        Instances test = source.getDataSet();
        test.setClassIndex(train.numAttributes() - 1);
        //////////////////////////////////////////////////////////////////////

        // Creamos filtros para no usar algunos de los atributos (columnas)
        Remove rm = new Remove();
        rm.setAttributeIndicesArray(new int[]{1,2,4}); // Removemos columnas agregando su indice aqui
        rm.setInputFormat(train);
        ///////////////////////////////////////////////////////////////////

        // Creamos un clasificador usando el algoritmo de arbol RepTree
        Classifier tree = new weka.classifiers.trees.REPTree();

        // Creamos un meta-clasificador que aplica los filtros antes de usar la data
        FilteredClassifier fc = new FilteredClassifier();
        fc.setFilter(rm);
        fc.setClassifier(tree);
        ////////////////////////////////////////////////////////////////////////////

        // Entrenamos el arbol (ajustamos el "modelo")
        fc.buildClassifier(train);

        // Serializamos el modelo para usar en el tp de agentes
        String location = "D:\\";
        weka.core.SerializationHelper.write(location + "\\reptree_1_2_4.model", fc);
        ///////////////////////////////////////////////////////

        // Imprimimos los resultados sobre la misma data (para calcular error de entrenamiento)
        for (int i = 0; i < test.numInstances(); i++) {
            double pred = fc.classifyInstance(test.instance(i));
            System.out.print("ID: " + i);
            System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
            System.out.println(", predicted: " + test.classAttribute().value((int) pred));
        }

        
    }
}
