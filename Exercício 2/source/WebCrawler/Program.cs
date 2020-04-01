using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using HtmlAgilityPack;

namespace WebCrawler
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Digite a quantidade máxima de iterações:");
            int qtdIt = int.Parse(Console.ReadLine());
            Console.WriteLine("Digite a url inicial:");
            string url = Console.ReadLine();
            
            do
            {
                try
                {
                    var initialPage = new Page()
                    {
                        Url = url
                    };
                    Persistence.Instance.SaveHtmlContent(initialPage);
                    HtmlRelevant.Instance.AddUnvisitedUrls(initialPage);
                }
                catch {
                    qtdIt++;
                }

                try
                {
                    url = HtmlRelevant.Instance.GetNextUrl();
                }
                catch
                {
                    break;
                }
                qtdIt--;
            } while (qtdIt > 0);


            Persistence.Instance.End();
        }
    }
}
