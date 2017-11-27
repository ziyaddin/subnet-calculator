import java.util.*;
import java.lang.*;

public class IP{

	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);
		List<Map> nets = new ArrayList();
		HashMap map;
		int net_hosts;
		String net_name, rest;
		int a, b, c, d;

		//read IPv4 with subnet prefix
		System.out.print("Please enter the given IP to subnet and # of subnets you want: ");
		String ip = sc.next();
		// String ip = "172.16.0.0/22";
		// String ip = "192.168.15.0/24";

		//split IPv4 structure to 4 portions and subnet prefix
		String[] octs = new String[4];
		octs = ip.split("\\.");
		int[] octets = new int[4];
		octets[0] = Integer.parseInt(octs[0]);
		octets[1] = Integer.parseInt(octs[1]);
		octets[2] = Integer.parseInt(octs[2]);
		octets[3] = Integer.parseInt(octs[3].split("/")[0]);
		int prefix = Integer.parseInt(octs[3].split("/")[1]);
		// System.out.println(prefix);

		//read # of subnets
		int tot_nets = sc.nextInt();
		//int tot_nets = 2;

		System.out.println("\nEnter name of the subnets and # of hosts per subnet below:\n");

		int count = 1;
		//read all networks' name and # of hosts
		while(count <= tot_nets){
			System.out.print("Subnet" + count + ". Name and # of hosts: ");
			net_name = sc.next();
			rest = sc.nextLine().replaceAll("\\s+","");
			net_hosts = 0;

			//check for empty WAN hosts
			if(rest.equals("")){
				if(net_name.toLowerCase().contains("wan")){
					net_hosts = 2;
				}
			}
			else{
				net_hosts = Integer.parseInt(rest);
			}

			map = new HashMap();
			map.put("name", net_name);
			map.put("hosts", net_hosts);
			nets.add(map);

			// System.out.println(net_hosts);
			count++;
		}

		class Sortbymaps implements Comparator<Map>
		{
			public int compare(Map a, Map b)
			{
				if((int) a.get("hosts") > (int) b.get("hosts")){
					return -1;
				}
				else if((int) a.get("hosts") < (int) b.get("hosts")){
					return 1;
				}
				else{
					return 0;
				}
			}
		}
		Collections.sort(nets, new Sortbymaps());
		// Collections.reverse(nets);

		System.out.println("\n\nResult:");
		System.out.printf("%-20s %-20s %-20s\n",  "Subnet Name", "Network add.", "Broadcast add.\n");

		count = 1;
		while(count <= tot_nets){
			net_hosts = (int)(nets.get(count - 1).get("hosts"));
			int n = net_hosts + 2;
			n--;
			n |= n >> 1;
			n |= n >> 2;
			n |= n >> 4;
			n |= n >> 8;
			n |= n >> 16;
			n++;
			net_hosts = n;

			int bits = (int) (Math.log(net_hosts) / Math.log(2));

			String res_name = (String)(nets.get(count - 1).get("name"));
			String res_ip = octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3] + "/" + (32 - bits);

			int net_broadcast = net_hosts - 1;

			d = net_broadcast % 256;
			c = (net_broadcast / 256) % 256;
			b = (net_broadcast / 256) / 256;
			a = ((net_broadcast / 256) / 256) % 256;

			octets[3] += d;
			octets[2] += c;
			octets[1] += b;
			octets[0] += a;

			String res_broad = octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3];

			octets[3] -= d;
			octets[2] -= c;
			octets[1] -= b;
			octets[0] -= a;

			d = net_hosts % 256;
			c = (net_hosts / 256) % 256;
			b = (net_hosts / 256) / 256;
			a = ((net_hosts / 256) / 256) % 256;

			octets[3] += d;
			octets[2] += c;
			octets[1] += b;
			octets[0] += a;

			System.out.printf("%-20s %-20s %-20s\n",  res_name, res_ip, res_broad);

			count++;
		}

		// System.out.println(nets);
	}
}
