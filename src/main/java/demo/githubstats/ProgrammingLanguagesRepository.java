package demo.githubstats;

import DIByRik.annotations.Repository;
import demo.githubstats.domain.ProgrammingLanguageRanking;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProgrammingLanguagesRepository {
	private final List<ProgrammingLanguageRanking> programmingLanguages = new ArrayList<>();

	public ProgrammingLanguagesRepository() {
		init();
	}

	public List<ProgrammingLanguageRanking> getAll() {
		return programmingLanguages;
	}

	public ProgrammingLanguageRanking getByPosition(int pos) {
		return programmingLanguages.stream().filter(pl -> pl.position() == pos).findFirst().orElse(null);
	}

	private void init() {
		programmingLanguages.add(new ProgrammingLanguageRanking(1, "Python", "16.756%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(2, "Java", "11.005%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(3, "C++", "10.254%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(4, "Go", "9.657%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(5, "JavaScript", "9.286%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(6, "TypeScript", "7.750%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(7, "PHP", "5.036%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(8, "Ruby", "4.948%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(9, "C", "4.271%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(10, "C#", "3.638%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(11, "Nix", "3.248%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(12, "Scala", "2.497%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(13, "Shell", "2.414%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(14, "Kotlin", "1.372%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(15, "Rust", "1.332%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(16, "Dart", "1.017%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(17, "Swift", "0.886%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(18, "DM", "0.407%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(19, "SystemVerilog", "0.330%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(20, "Lua", "0.326%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(21, "Perl", "0.317%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(22, "Groovy", "0.308%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(23, "Objective-C", "0.256%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(24, "Lean", "0.224%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(25, "Haskell", "0.213%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(26, "Elixir", "0.197%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(27, "OCaml", "0.176%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(28, "CodeQL", "0.168%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(29, "Clojure", "0.149%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(30, "PowerShell", "0.138%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(31, "Emacs Lisp", "0.117%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(32, "Erlang", "0.113%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(33, "Julia", "0.092%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(34, "F#", "0.089%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(35, "Bicep", "0.078%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(36, "Assembly", "0.067%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(37, "CoffeeScript", "0.066%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(38, "WebAssembly", "0.064%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(39, "Verilog", "0.063%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(40, "MATLAB", "0.057%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(41, "Visual Basic .NET", "0.047%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(42, "R", "0.046%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(43, "Puppet", "0.041%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(44, "GAP", "0.038%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(45, "SourcePawn", "0.036%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(46, "MLIR", "0.031%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(47, "ZAP", "0.029%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(48, "ANTLR", "0.028%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(49, "Vim Script", "0.027%"));
		programmingLanguages.add(new ProgrammingLanguageRanking(50, "hoon", "0.026%"));
	}
}
