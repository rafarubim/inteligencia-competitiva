using System;
using System.IO;
using System.Text.RegularExpressions;

namespace Artigo_IC_Clusters
{
  class Program
  {
    static void Main(string[] args)
    {
      string allFile = File.ReadAllText("results.txt");
      var rxWord = new Regex("WORD >>>>>.*\n.*");
      var matchCol = rxWord.Matches(allFile);
      Console.WriteLine(matchCol[0]);
    }
  }
}
