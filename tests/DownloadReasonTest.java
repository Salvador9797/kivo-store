package app.kivo.store;
public class DownloadReasonTest {
 static void has(String text,String expected){if(!text.contains(expected))throw new AssertionError(text);}
 public static void main(String[] args){has(DownloadReason.waiting(2),"Sin conexión");has(DownloadReason.waiting(3),"Wi-Fi");has(DownloadReason.failed(404),"Actualiza el catálogo");has(DownloadReason.failed(403),"rechazó");has(DownloadReason.failed(1006),"espacio");has(DownloadReason.failed(1008),"nueva");has(DownloadReason.failed(503),"más tarde");has(DownloadReason.failed(0),"servidor");System.out.println("PASS download recovery reasons for network, Wi-Fi, removed assets, access refusal, storage, resume failure and server errors");}
}
