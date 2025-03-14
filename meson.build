project('MemoryGame', 'java')

sources = [
  'src/MatchCards.java',
  'src/App.java',
]

resources = structured_sources([],{ 'img': files(
  'src/img/back.jpg',
  'src/img/darkness.jpg',
  'src/img/double.jpg',
  'src/img/fairy.jpg',
  'src/img/fighting.jpg',
  'src/img/fire.jpg',
  'src/img/grass.jpg',
  'src/img/lightning.jpg',
  'src/img/metal.jpg',
  'src/img/psychic.jpg',
  'src/img/water.jpg',
)})

game = jar(
  meson.project_name(),
  sources,
  main_class: 'App',
  java_resources: resources
)