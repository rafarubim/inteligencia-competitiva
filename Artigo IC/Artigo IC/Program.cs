using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Artigo_IC
{
  class Program
  {
    const string Context = "health";

    static async Task WhenAllReporting<TTask>(IEnumerable<Task<TTask>> tasks, Action<IEnumerable<Task<TTask>>> report)
    {
      var whenAll = Task.WhenAll(tasks);
      for (; ; )
      {
        var timer = Task.Delay(250);
        await Task.WhenAny(whenAll, timer);
        if (whenAll.IsCompleted)
        {
          return;
        }
        else
        {
          report(tasks);
        }
      }
    }

    static void Main(string[] args)
    {
      string[] lines = File.ReadAllLines("health.txt");
      string[] stopwords = File.ReadAllLines("stopwords.txt");
      var stopSet = new HashSet<string>();
      foreach (var stopword in stopwords)
      {
        stopSet.Add(stopword.ToLower());
      }
      var wordLines = new List<List<string>>();
      var rxWord = new Regex("^[a-zA-Z]+$");
      var rxNumberWord = new Regex("^[0-9]{4}\\|[a-zA-Z]+$");
      foreach (var line in lines)
      {
        string[] words = line.Split();
        var thisLine = new List<string>();
        foreach(var word in words)
        {
          string lowerWord = word;
          if (rxNumberWord.IsMatch(word))
          {
            lowerWord = word.Remove(0, 5);
          }
          lowerWord = lowerWord.ToLower();
          if (rxWord.IsMatch(lowerWord) && !stopSet.Contains(lowerWord))
          {
            thisLine.Add(lowerWord);
          }
        }
        wordLines.Add(thisLine);
      }
      IEnumerable<string> allWords = wordLines.SelectMany(line => line);
      int nWords = allWords.Count();

      var responseTsks = new List<(string word, Task<HttpResponseMessage> responseTsk)>();

      using (var client = new HttpClient())
      {
        var uriBuilder = new UriBuilder("https://api.datamuse.com/words");

        int i = 1;

        foreach (var word in allWords)
        {
          uriBuilder.Query = $"rel_trg={word}&topics={Context}";
          var responseTsk = client.GetAsync(uriBuilder.Uri);
          responseTsks.Add((word, responseTsk));
          Console.WriteLine($"Enviado request {i} de {nWords}");
          Task.Delay(10).Wait();

          i++;
        }
        WhenAllReporting(responseTsks.Select(tuple => tuple.responseTsk).ToArray(), tasks => Console.WriteLine($"Progress: {tasks.Count(tsk => tsk.IsCompleted)} of {nWords} completed"))
          .Wait();
      }
      
      var contentsWriter = new StringWriter();
      
      foreach (var tuple in responseTsks)
      {
        contentsWriter.WriteLine("WORD >>>>> "+tuple.word);
        using (var response = tuple.responseTsk.Result)
        {
          if (!response.IsSuccessStatusCode)
          {
            contentsWriter.WriteLine("<NO RESPONSE>");
          }
          else
          {
            var strTsk = response?.Content?.ReadAsStringAsync();
            strTsk?.Wait();
            var str = strTsk?.Result;
            if (str == null)
            {
              contentsWriter.WriteLine("<RESPONSE ERROR>");
            }
            else
            {
              contentsWriter.WriteLine(str);
            }
          }
        }
        contentsWriter.WriteLine("\n");
      }

      File.WriteAllText("results.txt", contentsWriter.ToString());
    }
  }
}
