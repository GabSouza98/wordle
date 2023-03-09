--select top 1 percent word from Words order by newid()
select word from words order by random() limit 1;