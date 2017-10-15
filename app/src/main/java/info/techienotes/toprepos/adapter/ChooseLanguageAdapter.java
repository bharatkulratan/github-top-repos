package info.techienotes.toprepos.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.techienotes.toprepos.R;

/**
 * Created by bharatkulratan
 */

public class ChooseLanguageAdapter extends BaseAdapter implements Filterable{

    private final int maxItems = 5;
    private final Context context;
    private LanguageFilter filter;
    private List<String> languageList;

    public ChooseLanguageAdapter(Context context){
        this.context = context;
        this.languageList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.language_item, parent, false);
        ((TextView)view.findViewById(R.id.langauge_name)).setText(languageList.get(position));
        return view;
    }

    @Override
    public Object getItem(int position) {
        if (languageList != null) {
            return languageList.get(position);
        }
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (languageList == null){
            return 0;
        }

        return Math.min(maxItems, languageList.size());
    }

    private final String[] languages={
            "Agda",
            "AGSScript",
            "Alloy",
            "AMPL",
            "ANTLR",
            "ApacheConf",
            "Apex",
            "APIBlueprint",
            "APL",
            "AppleScript",
            "Arc",
            "Arduino",
            "ASP",
            "AspectJ",
            "Assembly",
            "ATS",
            "Augeas",
            "AutoHotkey",
            "AutoIt",
            "Awk",
            "Batchfile",
            "Befunge",
            "Bison",
            "BitBake",
            "BlitzBasic",
            "BlitzMax",
            "Bluespec",
            "Boo",
            "Brainfuck",
            "Brightscript",
            "Bro",
            "C",
            "C#",
            "C++",
            "Cap'nProto",
            "CartoCSS",
            "Ceylon",
            "Chapel",
            "Charity",
            "ChucK",
            "Cirru",
            "Clarion",
            "Clean",
            "CLIPS",
            "Clojure",
            "CMake",
            "COBOL",
            "CoffeeScript",
            "ColdFusion",
            "CommonLisp",
            "ComponentPascal",
            "Cool",
            "Coq",
            "Crystal",
            "CSS",
            "Cucumber",
            "Cuda",
            "Cycript",
            "D",
            "DarcsPatch",
            "Dart",
            "Diff",
            "DIGITALCommandLanguage",
            "DM",
            "Dogescript",
            "DTrace",
            "Dylan",
            "E",
            "Eagle",
            "eC",
            "ECL",
            "Eiffel",
            "Elixir",
            "Elm",
            "EmacsLisp",
            "EmberScript",
            "Erlang",
            "F#",
            "Factor",
            "Fancy",
            "Fantom",
            "FLUX",
            "Forth",
            "FORTRAN",
            "Frege",
            "GameMakerLanguage",
            "GAMS",
            "GAP",
            "GDScript",
            "Genshi",
            "GettextCatalog",
            "GLSL",
            "Glyph",
            "Gnuplot",
            "Go",
            "Golo",
            "Gosu",
            "Grace",
            "GrammaticalFramework",
            "Groff",
            "Groovy",
            "Hack",
            "Handlebars",
            "Harbour",
            "Haskell",
            "Haxe",
            "HCL",
            "HTML",
            "Hy",
            "HyPhy",
            "IDL",
            "Idris",
            "IGORPro",
            "Inform7",
            "InnoSetup",
            "Io",
            "Ioke",
            "Isabelle",
            "J",
            "Jasmin",
            "Java",
            "JavaScript",
            "JFlex",
            "JSONiq",
            "Julia",
            "KiCad",
            "Kit",
            "Kotlin",
            "KRL",
            "LabVIEW",
            "Lasso",
            "Lean",
            "Lex",
            "LilyPond",
            "Limbo",
            "Liquid",
            "LiveScript",
            "LLVM",
            "Logos",
            "Logtalk",
            "LOLCODE",
            "LookML",
            "LoomScript",
            "LSL",
            "Lua",
            "M",
            "Makefile",
            "Mako",
            "Markdown",
            "Mask",
            "Mathematica",
            "Matlab",
            "Max",
            "Mercury",
            "MiniD",
            "Mirah",
            "Modelica",
            "Modula-2",
            "ModuleManagementSystem",
            "Monkey",
            "Moocode",
            "MoonScript",
            "MTML",
            "mupad",
            "Myghty",
            "NCL",
            "Nemerle",
            "nesC",
            "NetLinx",
            "NetLinx+ERB",
            "NetLogo",
            "NewLisp",
            "Nginx",
            "Nimrod",
            "Nit",
            "Nix",
            "NSIS",
            "Nu",
            "Objective-C",
            "Objective-C++",
            "Objective-J",
            "OCaml",
            "Omgrofl",
            "ooc",
            "Opa",
            "Opal",
            "OpenEdgeABL",
            "OpenSCAD",
            "Ox",
            "Oxygene",
            "Oz",
            "Pan",
            "Papyrus",
            "Parrot",
            "Pascal",
            "PAWN",
            "Perl",
            "Perl6",
            "PHP",
            "PicoLisp",
            "PigLatin",
            "Pike",
            "PLpgSQL",
            "PLSQL",
            "PogoScript",
            "PostScript",
            "PowerShell",
            "Processing",
            "Prolog",
            "PropellerSpin",
            "ProtocolBuffer",
            "Puppet",
            "PureData",
            "PureBasic",
            "PureScript",
            "Python",
            "QMake",
            "QML",
            "R",
            "Racket",
            "RagelinRubyHost",
            "RAML",
            "RDoc",
            "REALbasic",
            "Rebol",
            "Red",
            "Redcode",
            "RenderScript",
            "RobotFramework",
            "Rouge",
            "Ruby",
            "Rust",
            "SaltStack",
            "SAS",
            "Scala",
            "Scheme",
            "Scilab",
            "Self",
            "Shell",
            "ShellSession",
            "Shen",
            "Slash",
            "Smali",
            "Smalltalk",
            "Smarty",
            "SMT",
            "SourcePawn",
            "SQF",
            "SQL",
            "SQLPL",
            "Squirrel",
            "StandardML",
            "Stata",
            "SuperCollider",
            "Swift",
            "SystemVerilog",
            "Tcl",
            "Tea",
            "TeX",
            "Thrift",
            "Turing",
            "TXL",
            "TypeScript",
            "UnrealScript",
            "Vala",
            "VCL",
            "Verilog",
            "VHDL",
            "VimL",
            "VisualBasic",
            "Volt",
            "Vue",
            "WebOntologyLanguage",
            "WebIDL",
            "wisp",
            "X10",
            "xBase",
            "XC",
            "XML",
            "Xojo",
            "XPages",
            "XProc",
            "XQuery",
            "XS",
            "XSLT",
            "Xtend",
            "Yacc",
            "Zephir",
            "Zimpl",
    };

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new LanguageFilter();
        }
        return filter;
    }


    private class LanguageFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence input) {
            FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(input)){
                return filterResults;
            }

            if (languageList == null){
                languageList = new ArrayList<>();
            }else {
                languageList.clear();
            }

            String inputStr = input.toString().toLowerCase();

            // first add languages whose name starts with input string
            for(String item:languages) {
                String sourceStr = item.toLowerCase();

                if (sourceStr.startsWith(inputStr)){
                    languageList.add(item);
                }
            }

            // later add those elements which contains input string
            for (String item: languages){
                String sourceStr = item.toLowerCase();

                if (sourceStr.contains(inputStr) &&
                        !sourceStr.startsWith(inputStr)){
                    // to prevent duplicate addition of item
                    languageList.add(item);
                }
            }

            filterResults.values = languageList;
            filterResults.count = languageList.size();
            return filterResults;
        }

        @Override
        protected void
        publishResults(CharSequence constraint, FilterResults results) {
            // this should not happen ideally
            if (results == null){
                return;
            }

            languageList = (List<String>) results.values;

            notifyDataSetChanged();
        }
    }


}
