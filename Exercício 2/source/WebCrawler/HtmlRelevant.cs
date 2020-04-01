using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WebCrawler
{
    public class HtmlRelevant
    {
        private HtmlRelevant() { }

        private static HtmlRelevant _instance = null;

        private HashSet<string> VisitedUrls = new HashSet<string>();

        private Queue<string> Urls = new Queue<string>();

        public static HtmlRelevant Instance
        {
            get
            {
                if (_instance == null)
                {
                    _instance = new HtmlRelevant();
                }
                return _instance;
            }
        }

        public void AddUnvisitedUrls(Page page)
        {
            HtmlNodeCollection attrNodes = page.Document.DocumentNode.SelectNodes("//*[@*]");
            if (attrNodes != null)
            {
                foreach (var node in attrNodes)
                {
                    if (IsContentTag(node.Name))
                    {
                        HtmlAttributeCollection attributes = node.Attributes;
                        foreach (var attr in attributes)
                        {
                            if (IsUrl(attr.Value))
                            {
                                TryEnqueueUnvisited(attr.Value);
                            }
                        }
                    }
                }
            }
            
            HtmlNodeCollection nodes = page.Document.DocumentNode.SelectNodes("//node()");
            if (nodes != null)
            {
                foreach (var node in nodes)
                {
                    if (node.NodeType == HtmlNodeType.Text)
                    {
                        if (IsContentTag(node.ParentNode.Name) && IsUrl(node.InnerText))
                        {
                            TryEnqueueUnvisited(node.InnerText);
                        }
                    }
                }

            }
        }

        public string GetNextUrl()
        {
            return Urls.Dequeue();
        }

        public bool IsUrl(string str)
        {
            if ((str.StartsWith("http://") || str.StartsWith("https://")) && !str.EndsWith(".png")
                && !str.EndsWith(".ico") && !str.EndsWith(".gif") && !str.EndsWith(".jpg") && !str.EndsWith(".jpeg") && !str.EndsWith(".css")
                && !str.EndsWith(".mp3") && !str.EndsWith(".mp4") && !str.EndsWith(".avi"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool IsContentTag(string tag)
        {
            switch(tag)
            {
                case "script": case "style":
                case "html":
                    return false;
                default:
                    return true;
            }
        }

        private bool TryEnqueueUnvisited(string url)
        {
            string commonUrl = ConvertToCommonUrl(url);
            if (VisitedUrls.Contains(commonUrl))
            {
                return false;
            }
            else
            {
                Urls.Enqueue(url);
                VisitedUrls.Add(commonUrl);
                Persistence.Instance.AppendUrls(new string[] { url });
                return true;
            }
        }

        private string ConvertToCommonUrl(string url)
        {
            string commonUrl = url;
            if (url.StartsWith("http://"))
            {
                commonUrl = commonUrl.Remove(0, 7);
            }
            else if (url.StartsWith("https://"))
            {
                commonUrl = commonUrl.Remove(0, 8);
            }
            if (url.EndsWith("/"))
            {
                commonUrl = commonUrl.Remove(commonUrl.Length-1);
            }
            return commonUrl;
        }
    }
}
