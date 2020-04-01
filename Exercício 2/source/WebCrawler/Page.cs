using HtmlAgilityPack;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace WebCrawler
{
    public class Page
    {
        private HtmlDocument _document;

        private string _url;

        public string Url {
            get
            {
                return _url;
            }
            set
            {
                _url = value;
                _document = null;
            }
        }

        public HtmlDocument Document {
            get
            {
                if (_url == null)
                {
                    return null;
                }
                if (_document == null)
                {
                    using (WebClient client = new WebClient())
                    {
                        string outStr = client.DownloadString(_url);
                        _document = new HtmlDocument();
                        _document.LoadHtml(outStr);
                    }
                }
                return _document;
            }
        }
    }
}
