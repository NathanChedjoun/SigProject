<!DOCTYPE html>
<html>

<head>
  <title>Projet Webmapping</title>
  <meta charset="utf-8">
  <meta content="ie=edge" http-equiv="x-ua-compatible">
  <meta content="Projet Webmapping" name="description">
  <meta content="width=device-width,initial-scale=1,user-scalable=0" name="viewport">
  <!--<link href="apple-touch-icon.png" rel="apple-touch-icon">-->

  <!-- site-wide styles -->
  <link href="assets/stylesheets/bootstrap.css" rel="stylesheet">
  <link href="assets/stylesheets/site.css" rel="stylesheet">
  <link rel="stylesheet" href="assets/stylesheets/switchery.css">
  <link href="https://cdn.rawgit.com/openlayers/openlayers.github.io/master/en/v5.3.0/css/ol.css" rel="stylesheet" type="text/css">
  <link href="assets/stylesheets/style.css" rel="stylesheet" />

  <!-- current page styles -->
  <link href="assets/stylesheets/demos.css" rel="stylesheet">
  <link href="assets/stylesheets/font-awesome.css" rel="stylesheet">
  <link href="assets/stylesheets/material-design-iconic-font.min.css" rel="stylesheet">
  <link href="https:/assets/fonts.googleapis.com/css?family=Roboto:300,400,500,600" rel="stylesheet">
  <script src="https://cdn.rawgit.com/openlayers/openlayers.github.io/master/en/v5.3.0/build/ol.js"></script>
  <script src="assets/javascripts/breakpoints.min.js"></script>
  <script>Breakpoints({
      xs: { min: 0, max: 575 },
      sm: { min: 576, max: 767 },
      md: { min: 768, max: 991 },
      lg: { min: 992, max: 1199 },
      xl: { min: 1200, max: Infinity }
    });</script>
</head>

<body class="menubar-top menubar-light demo-examples">
  <!--[if lt IE 10]>
	<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
		your browser</a> to improve your experience.</p>
	<![endif]-->
  <nav class="site-navbar navbar fixed-top navbar-expand-lg navbar-light bg-blue">
    <div class="navbar-header">
      <a class="navbar-brand" href="index.html">
        <span class="brand-name hidden-fold">Projet WebMapping</span>
      </a>

      <button class="navbar-toggler hidden-lg-up collapsed" data-toggle="navbar-search" type="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="zmdi zmdi-hc-lg zmdi-search"></span>
      </button>

      <button aria-expanded="false" class="navbar-toggler hidden-lg-up collapsed" data-target="#site-navbar-collapse"
        data-toggle="collapse" type="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="zmdi zmdi-hc-lg zmdi-more"></span>
      </button>
    </div><!-- /.navbar-header -->

    <div class="collapse navbar-collapse" id="site-navbar-collapse">
      <ul class="navbar-nav ml-auto">
        <li class="nav-item">
          <span>
            ENSP 5GI - 2019
          </span>
        </li>
      </ul>

    </div><!-- /.collapse -->
  </nav><!-- /.site-navbar -->
  <header class="site-header">
    <div class="jumbotron jumbotron-fluid">
      <div class="jumbotron-text">
        <h3 class="text-primary text-center">Bienvenue Sur Notre Plateforme Webmapping</h3>
        <!--<small class="font-italic text-muted">Bootstrap 4 Web App Kit</small>-->
      </div><!-- /.jumbotron-text -->

    </div><!-- /.jumbotron -->

    <div class="breadcrumb">
      <ol class="breadcrumb-tree">
        <li class="breadcrumb-item">
          <a href="#">
            <span class="zmdi zmdi-home mr-1"></span>
            <span>Home</span>
          </a>
        </li>
      </ol>
    </div><!-- /.breadcrumb -->
  </header><!-- /.site-header -->

  <main class="site-main">
      <div class="site-content">
          <div class="row">
              <div class="col-lg-9 col-sm-9 col-md-9">
                  <div class="card">
                      <header class="card-header d-flex justify-content-between">
                          <h6 class="card-heading">Carte Cameroun</h6>
                          <div class="col-md-8 ml-auto">
                              <div class="input-group">
                                  <select class="custom-select" id="cqlAttribut">
                                      <option selected disabled value="">Attribut...</option>                  
                                  </select>
                                  <select class="custom-select" id="cqlOperateur">
                                      <option selected disabled value="">Operateur...</option>                                    
                                  </select>
                                  <input type="text" class="form-control" id="cqlValue" aria-label="valeur" placeholder="valeur">
                                  <div class="input-group-append">
                                      <button class="btn btn-outline-dark" id="filter" type="button">Filtrer</button>
                                      <button class="btn btn-outline-dark" id="reset" type="button">Reset</button>
                                  </div>
                              </div>
                          </div>
                      </header>
                      <div>
                          <div id="vmap-world1"></div>
                          <div id="info">&nbsp;</div>
                      </div>
                  </div><!-- /.card -->
              </div><!-- /.col -->
              <div class="col-lg-3 col-sm-3 col-md-3 h-100">
                  <div class="card">
                      <header class="card-header">Gestion des couches</header>
                      <div class="card-body">
                          <div id='layercard'>
                              <header>
                                  <h6 class="text-primary text-center"> Liste des couches</h6>
                              </header>
                              <ul class="list-group-flush list-group" id="layerList"></ul>
                          </div>
                          <hr />
                          <div class="row">
                              <div class="col-md-8 m-auto">
                                  <button class="btn btn-outline-dark btn-block" data-target="#addlayer" data-toggle="modal">
                                      Ajouter Couche
                                  </button>
                              </div>
                          </div>
                      </div>
                  </div><!-- /.card -->
              </div><!-- /.col -->
          </div><!-- /.row -->
      </div>
  </main><!-- /.site-main -->

  <footer class="site-footer">
    <div class="mr-auto">
      <p class="text-primary mb-0">Made By Groupe #</p>
    </div>
  </footer><!-- /.site-footer -->

  <div aria-hidden="true" aria-labelledby="addLayer" class="modal fade" id="addlayer" role="dialog" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
      <form class="modal-content" action="/SigProject/accueil" method="post" enctype="multipart/form-data">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Charger une nouvelle couche</h5>
          <button aria-label="Close" class="close" data-dismiss="modal" type="button">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row">
            <div class="col-md-12">
              <div class="m-auto">
                <div class="input-group">
                  <div class="custom-file">
                    <input class="custom-file-input" id="customFile" type="file" name="shapefile" accept=".shp, .dbf">
                    <label class="custom-file-label" for="customFile">Choisir Le ShapeFile De La Couche à Charger</label>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer row justify-content-end">
          <div class="col-md-3">
            <button class="btn btn-success btn-block" type="submit"><i class="fa fa-download"
                style="font-size: 16px;"></i>Ajouter</button>
          </div>
          <div class="col-md-3">
            <button class="btn btn-danger btn-block" data-dismiss="modal" type="button">Close</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</body>
<!-- core plugins -->
<script> 
    var obj = ${ layerGroup }; 
</script>

<script src="assets/javascripts/jquery.min.js"></script>
<script src="assets/javascripts/popper.min.js"></script>
<script src="assets/javascripts/bootstrap.js"></script>
<script src="assets/javascripts/jquery.waypoints.min.js"></script>
<script src="assets/javascripts/switchery.js"></script>
<script src="assets/javascripts/main.js"></script>
<script src="assets/javascripts/map_test.js"></script>

</html>