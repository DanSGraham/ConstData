package danandzach.labpal;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * A class to make checking for multiple saerch queries as the same element easier.
 * Created by Daniel on 8/26/2015.
 */
public class PeriodicTable {
    //A class to store basic atomic values. -D
    private static HashSet<Element> PeriodicTableSet;
    public static int periodic_table_size;

    public PeriodicTable(){
        //Initializes the set and adds all elements
        PeriodicTableSet = new HashSet<Element>();
        PeriodicTableSet.add(new Element("Hydrogen", "H", 1));
        PeriodicTableSet.add(new Element("Helium", "He", 2));
        PeriodicTableSet.add(new Element("Lithium", "Li", 3));
        PeriodicTableSet.add(new Element("Beryllium", "Be", 4));
        PeriodicTableSet.add(new Element("Boron", "B", 5));
        PeriodicTableSet.add(new Element("Carbon", "C", 6));
        PeriodicTableSet.add(new Element("Nitrogen", "N", 7));
        PeriodicTableSet.add(new Element("Oxygen", "O", 8));
        PeriodicTableSet.add(new Element("Fluorine", "F", 9));
        PeriodicTableSet.add(new Element("Neon", "Ne", 10));
        PeriodicTableSet.add(new Element("Sodium", "Na", 11));
        PeriodicTableSet.add(new Element("Magnesium", "Mg", 12));
        PeriodicTableSet.add(new Element("Aluminium", "Al", 13));
        PeriodicTableSet.add(new Element("Silicon", "Si", 14));
        PeriodicTableSet.add(new Element("Phosphorus", "P", 15));
        PeriodicTableSet.add(new Element("Sulfur", "S", 16));
        PeriodicTableSet.add(new Element("Chlorine", "Cl", 17));
        PeriodicTableSet.add(new Element("Argon", "Ar", 18));
        PeriodicTableSet.add(new Element("Potassium", "K", 19));
        PeriodicTableSet.add(new Element("Calcium", "Ca", 20));
        PeriodicTableSet.add(new Element("Scandium", "Sc", 21));
        PeriodicTableSet.add(new Element("Titanium", "Ti", 22));
        PeriodicTableSet.add(new Element("Vanadium", "V", 23));
        PeriodicTableSet.add(new Element("Chromium", "Cr", 24));
        PeriodicTableSet.add(new Element("Manganese", "Mn", 25));
        PeriodicTableSet.add(new Element("Iron", "Fe", 26));
        PeriodicTableSet.add(new Element("Cobalt", "Co", 27));
        PeriodicTableSet.add(new Element("Nickel", "Ni", 28));
        PeriodicTableSet.add(new Element("Copper", "Cu", 29));
        PeriodicTableSet.add(new Element("Zinc", "Zn", 30));
        PeriodicTableSet.add(new Element("Gallium", "Ga", 31));
        PeriodicTableSet.add(new Element("Germanium", "Ge", 32));
        PeriodicTableSet.add(new Element("Arsenic", "As", 33));
        PeriodicTableSet.add(new Element("Selenium", "Se", 34));
        PeriodicTableSet.add(new Element("Bromine", "Br", 35));
        PeriodicTableSet.add(new Element("Krypton", "Kr", 36));
        PeriodicTableSet.add(new Element("Rubidium", "Rb", 37));
        PeriodicTableSet.add(new Element("Strontium", "Sr", 38));
        PeriodicTableSet.add(new Element("Yttrium", "Y", 39));
        PeriodicTableSet.add(new Element("Zirconium", "Zr", 40));
        PeriodicTableSet.add(new Element("Niobium", "Nb", 41));
        PeriodicTableSet.add(new Element("Molybdenum", "Mo", 42));
        PeriodicTableSet.add(new Element("Technetium", "Tc", 43));
        PeriodicTableSet.add(new Element("Ruthenium", "Ru", 44));
        PeriodicTableSet.add(new Element("Rhodium", "Rh", 45));
        PeriodicTableSet.add(new Element("Palladium", "Pd", 46));
        PeriodicTableSet.add(new Element("Silver", "Ag", 47));
        PeriodicTableSet.add(new Element("Cadmium", "Cd", 48));
        PeriodicTableSet.add(new Element("Indium", "In", 49));
        PeriodicTableSet.add(new Element("Tin", "Sn", 50));
        PeriodicTableSet.add(new Element("Antimony", "Sb", 51));
        PeriodicTableSet.add(new Element("Tellurium", "Te", 52));
        PeriodicTableSet.add(new Element("Iodine", "I", 53));
        PeriodicTableSet.add(new Element("Xenon", "Xe", 54));
        PeriodicTableSet.add(new Element("Caesium", "Cs", 55));
        PeriodicTableSet.add(new Element("Barium", "Ba", 56));
        PeriodicTableSet.add(new Element("Lanthanum", "La", 57));
        PeriodicTableSet.add(new Element("Cerium", "Ce", 58));
        PeriodicTableSet.add(new Element("Praseodymium", "Pr", 59));
        PeriodicTableSet.add(new Element("Neodymium", "Nd", 60));
        PeriodicTableSet.add(new Element("Promethium", "Pm", 61));
        PeriodicTableSet.add(new Element("Samarium", "Sm", 62));
        PeriodicTableSet.add(new Element("Europium", "Eu", 63));
        PeriodicTableSet.add(new Element("Gadolinium", "Gd", 64));
        PeriodicTableSet.add(new Element("Terbium", "Tb", 65));
        PeriodicTableSet.add(new Element("Dysprosium", "Dy", 66));
        PeriodicTableSet.add(new Element("Holmium", "Ho", 67));
        PeriodicTableSet.add(new Element("Erbium", "Er", 68));
        PeriodicTableSet.add(new Element("Thulium", "Tm", 69));
        PeriodicTableSet.add(new Element("Ytterbium", "Yb", 70));
        PeriodicTableSet.add(new Element("Lutetium", "Lu", 71));
        PeriodicTableSet.add(new Element("Hafnium", "Hf", 72));
        PeriodicTableSet.add(new Element("Tantalum", "Ta", 73));
        PeriodicTableSet.add(new Element("Tungsten", "W", 74));
        PeriodicTableSet.add(new Element("Rhenium", "Re", 75));
        PeriodicTableSet.add(new Element("Osmium", "Os", 76));
        PeriodicTableSet.add(new Element("Iridium", "Ir", 77));
        PeriodicTableSet.add(new Element("Platinum", "Pt", 78));
        PeriodicTableSet.add(new Element("Gold", "Au", 79));
        PeriodicTableSet.add(new Element("Mercury", "Hg", 80));
        PeriodicTableSet.add(new Element("Thallium", "Tl", 81));
        PeriodicTableSet.add(new Element("Lead", "Pb", 82));
        PeriodicTableSet.add(new Element("Bismuth", "Bi", 83));
        PeriodicTableSet.add(new Element("Polonium", "Po", 84));
        PeriodicTableSet.add(new Element("Astatine", "At", 85));
        PeriodicTableSet.add(new Element("Radon", "Rn", 86));
        PeriodicTableSet.add(new Element("Francium", "Fr", 87));
        PeriodicTableSet.add(new Element("Radium", "Ra", 88));
        PeriodicTableSet.add(new Element("Actinium", "Ac", 89));
        PeriodicTableSet.add(new Element("Thorium", "Th", 90));
        PeriodicTableSet.add(new Element("Protactinium", "Pa", 91));
        PeriodicTableSet.add(new Element("Uranium", "U", 92));
        PeriodicTableSet.add(new Element("Neptunium", "Np", 93));
        PeriodicTableSet.add(new Element("Plutonium", "Pu", 94));
        PeriodicTableSet.add(new Element("Americium", "Am", 95));
        PeriodicTableSet.add(new Element("Curium", "Cm", 96));
        PeriodicTableSet.add(new Element("Berkelium", "Bk", 97));
        PeriodicTableSet.add(new Element("Californium", "Cf", 98));
        PeriodicTableSet.add(new Element("Einsteinium", "Es", 99));
        PeriodicTableSet.add(new Element("Fermium", "Fm", 100));
        PeriodicTableSet.add(new Element("Mendelevium", "Md", 101));
        PeriodicTableSet.add(new Element("Nobelium", "No", 102));
        PeriodicTableSet.add(new Element("Lawrencium", "Lr", 103));
        PeriodicTableSet.add(new Element("Rutherfordium", "Rf", 104));
        PeriodicTableSet.add(new Element("Dubnium", "Db", 105));
        PeriodicTableSet.add(new Element("Seaborgium", "Sg", 106));
        PeriodicTableSet.add(new Element("Bohrium", "Bh", 107));
        PeriodicTableSet.add(new Element("Hassium", "Hs", 108));
        PeriodicTableSet.add(new Element("Meitnerium", "Mt", 109));
        PeriodicTableSet.add(new Element("Darmstadtium", "Ds", 110));
        PeriodicTableSet.add(new Element("Roentgenium", "Rg", 111));
        PeriodicTableSet.add(new Element("Copernicium", "Cn", 112));
        PeriodicTableSet.add(new Element("Ununtrium", "Uut", 113));
        PeriodicTableSet.add(new Element("Flerovium", "Fl", 114));
        PeriodicTableSet.add(new Element("Ununpentium", "Uup", 115));
        PeriodicTableSet.add(new Element("Livermorium", "Lv", 116));
        PeriodicTableSet.add(new Element("Ununseptium", "Uus", 117));
        PeriodicTableSet.add(new Element("Ununoctium", "Uuo", 118));
        periodic_table_size = PeriodicTableSet.size();

    }

    public boolean isElementName(String nameToCheck){
        for (Element element : PeriodicTableSet){
            if(nameToCheck.trim().equalsIgnoreCase(element.getElementName())){
                return true;
            }
        }
        return false;
    }

    public boolean isElementSymbol(String symbolToCheck){
        for(Element element: PeriodicTableSet){
            if(symbolToCheck.trim().equalsIgnoreCase(element.getElementSymbol())){
                return true;
            }
        }
        return false;
    }

    public String getElementName(int z){
        for(Element element: PeriodicTableSet){
            if(z == element.getAtomicNumber()){
                return element.getElementName();
            }
        }
        return null;
    }

    public String getElementName(String elementSymbol){

        for(Element element: PeriodicTableSet){
            if(elementSymbol.trim().equalsIgnoreCase(element.getElementSymbol())){
                return element.getElementName();
            }
        }

        return null;
    }

    public String getElementSymbol(int z){
        for(Element element: PeriodicTableSet){
            if(z == element.getAtomicNumber()){
                return element.getElementSymbol();
            }
        }
        return null;
    }

    public String getElementSymbol(String elementName){

        for(Element element: PeriodicTableSet){
            if(elementName.trim().equalsIgnoreCase(element.getElementName())){
                return element.getElementSymbol();
            }
        }
        return null;
    }

    public int getAtomicNumber(String queryString){
        for(Element element: PeriodicTableSet){
            if((queryString.trim().equalsIgnoreCase(element.getElementName())) || queryString.trim().equalsIgnoreCase(element.getElementSymbol())){
                return element.getAtomicNumber();
            }
        }
        return -1;
    }

    public static HashSet<Element> getPeriodicTableSet(){
        return PeriodicTableSet;
    }
}

class Element{
    //A class to define elements and element values. -D
    private String elementName;
    private String elementSymbol;
    private int atomicNumber;

    public Element(String initElementName, String initElementSymbol, int initAtomicNumber){
        setElementName(initElementName);
        setElementSymbol(initElementSymbol);
        setAtomicNumber(initAtomicNumber);
    }


    public void setElementName(String elementNameToSet){
        elementName = elementNameToSet;
    }

    public void setElementSymbol(String elementSymbolToSet){
        elementSymbol = elementSymbolToSet;
    }

    public void setAtomicNumber(int atomicNumberToSet){
        atomicNumber = atomicNumberToSet;
    }

    public String getElementName(){
        return elementName;
    }

    public String getElementSymbol(){
        return elementSymbol;
    }

    public int getAtomicNumber(){
        return atomicNumber;
    }

    @Override
    public String toString(){
        return elementName;
    }
}
