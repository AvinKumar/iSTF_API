/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SupervisorAPI;

/**
 *
 * @author sayedmo
 */
public class Testmain {
    public static void main(String[] args) throws Exception {
//        CompareDSearch readXMLModel= new CompareDSearch();
//        readXMLModel.compare_hped_kwys("6");
        CompareCompSearch readXMLModel= new CompareCompSearch();
       readXMLModel.compare_comp_search("3");

        
    }
}
