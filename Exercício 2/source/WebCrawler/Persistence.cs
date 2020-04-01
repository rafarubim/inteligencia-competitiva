using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HtmlAgilityPack;

namespace WebCrawler
{
    public class Persistence
    {
        private Persistence() {
            File.Delete(UrlsFilePath);
            File.Delete(ContentFilePath);
            _urlsStrm = File.OpenWrite(UrlsFilePath);
            _urlsWriter = new StreamWriter(_urlsStrm);
            _contentStrm = File.OpenWrite(ContentFilePath);
            _contentWriter = new StreamWriter(_contentStrm);
        }

        private static Persistence _instance = null;

        private FileStream _urlsStrm;

        private StreamWriter _urlsWriter;

        private FileStream _contentStrm;

        private StreamWriter _contentWriter;

        public static Persistence Instance
        {
            get
            {
                if (_instance == null)
                {
                    _instance = new Persistence();
                }
                return _instance;
            }
        }

        private const string ContentFilePath = "HtmlContent.txt";

        private const string UrlsFilePath = "Urls.txt";
        
        public bool SaveHtmlContent(Page page)
        {
            HtmlNodeCollection nodes = page.Document.DocumentNode.SelectNodes("//node()");
            if (nodes != null)
            {
                _contentWriter.WriteLine("\r\nURL: "+page.Url+"\r\n\r\n");
                bool somethingWritten = false;
                foreach (var node in nodes)
                {
                    if (!HtmlRelevant.Instance.IsContentTag(node.ParentNode.Name))
                    {
                        continue;
                    }
                    if (node.NodeType == HtmlNodeType.Text)
                    {
                        if (!string.IsNullOrWhiteSpace(node.InnerText))
                        {
                            _contentWriter.WriteLine(node.InnerText);
                        }
                    }
                }
                if (somethingWritten)
                {
                    return true;
                }
                else
                {
                    return false;
                }
                    
            }
            else
            {
                return false;
            }
        }

        public void AppendUrls(IEnumerable<string> urls)
        {
            foreach (var url in urls)
            {
                _urlsWriter.WriteLine(url);
            }
        }

        public void End()
        {
            _urlsWriter.Dispose();
            _urlsStrm.Dispose();
            _contentWriter.Dispose();
            _contentStrm.Dispose();
        }


        public string ConvertToFilePath(string fileName)
        {
            return fileName.Replace("@", "@0")
                            .Replace("\\", "@1")
                            .Replace("/", "@2")
                            .Replace(":", "@3")
                            .Replace("\"", "@4")
                            .Replace("<", "@5")
                            .Replace(">", "@6")
                            .Replace("?", "@7")
                            .Replace("|", "@8")
                            .Replace("*", "@9");
        }
    }
}
