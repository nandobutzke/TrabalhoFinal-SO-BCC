package start;

import java.io.IOException;
import java.util.stream.Collectors;

public class Teste {

	public static void main(String[] args) throws IOException {
		//1
//		Process process = Runtime.getRuntime().exec("tasklist.exe");
//	    Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
//	    while (scanner.hasNext()) {
//	        System.out.println(scanner.nextLine());
//	    }
//	    scanner.close();
		
	    //2
		ProcessHandle.allProcesses().forEach(proc -> getProcess(proc));
	}

	private static void getProcess(ProcessHandle proc) {
		System.out.println("PID: " + proc.pid() + " User: " + proc.info().user().orElse("") 
				+ " Comando: " + proc.info().commandLine().orElse("") + " Linha:" + proc.info().command().orElse(""));
		
		if(proc.info().command().orElse("").toLowerCase().contains("discord".toLowerCase())) {
			proc.destroy();
			System.out.println("a");
			for(ProcessHandle p : proc.children().collect(Collectors.toList())) {
				System.out.println(p.info().command().orElse(""));
			}
		}
	}

}
